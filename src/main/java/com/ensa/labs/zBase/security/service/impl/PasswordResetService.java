package com.ensa.labs.zBase.security.service.impl;

import com.ensa.labs.exception.GlobalException;
import com.ensa.labs.zBase.mail.MailService;
import com.ensa.labs.zBase.security.bean.PasswordResetToken;
import com.ensa.labs.zBase.security.bean.User;
import com.ensa.labs.zBase.security.bean.enums.UserStatus;
import com.ensa.labs.zBase.security.dao.facade.PasswordResetTokenDao;
import com.ensa.labs.zBase.security.dao.facade.UserDao;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.util.HtmlUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Base64;
import java.util.Deque;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * "Forgot password" flow: a single-use link valid for a short time is e-mailed to the address of
 * the account, and lets its owner choose a new password.
 *
 * <p>The answer to a request never says whether the address belongs to an account, and the e-mail
 * is sent in the background so the response time does not tell it either.</p>
 */
@Service
public class PasswordResetService {

    private static final Logger log = LoggerFactory.getLogger(PasswordResetService.class);

    static final int MIN_PASSWORD_LENGTH = 8;
    /** Links one account may receive per hour. */
    static final int MAX_LINKS_PER_ACCOUNT_PER_HOUR = 3;
    /** Requests one client address may make per hour. */
    static final int MAX_REQUESTS_PER_CLIENT_PER_HOUR = 10;

    private final UserDao userDao;
    private final PasswordResetTokenDao tokenDao;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final String publicUrl;
    private final Duration validity;
    private final Clock clock;
    private final SecureRandom random = new SecureRandom();
    private final Map<String, Deque<Instant>> requestsByClient = new ConcurrentHashMap<>();
    private final ExecutorService mailExecutor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable, "password-reset-mail");
        thread.setDaemon(true);
        return thread;
    });

    @Autowired
    public PasswordResetService(UserDao userDao,
                                PasswordResetTokenDao tokenDao,
                                PasswordEncoder passwordEncoder,
                                MailService mailService,
                                @Value("${app.public-url:http://localhost:4200}") String publicUrl,
                                @Value("${app.password-reset.token-validity-minutes:30}") long validityMinutes) {
        this(userDao, tokenDao, passwordEncoder, mailService, publicUrl, Duration.ofMinutes(validityMinutes), Clock.systemUTC());
    }

    PasswordResetService(UserDao userDao, PasswordResetTokenDao tokenDao, PasswordEncoder passwordEncoder,
                         MailService mailService, String publicUrl, Duration validity, Clock clock) {
        this.userDao = userDao;
        this.tokenDao = tokenDao;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.publicUrl = publicUrl.replaceAll("/+$", "");
        this.validity = validity;
        this.clock = clock;
    }

    /**
     * Sends a reset link when the address belongs to an active account. Always returns normally
     * (unless the client is over its request limit), whatever the address.
     */
    @Transactional
    public void requestReset(String email, String clientAddress) {
        Instant now = clock.instant();
        if (!allowClient(clientAddress, now)) {
            throw new GlobalException(HttpStatus.TOO_MANY_REQUESTS,
                    "Trop de demandes. Réessayez dans une heure.");
        }
        String address = email == null ? "" : email.trim();
        if (address.isEmpty() || !mailService.isEnabled()) {
            return;
        }
        // Two accounts sharing an address (different case) would make the owner ambiguous: no link then.
        List<User> matches = userDao.findTop2ByEmailIgnoreCase(address);
        User user = matches.size() == 1 ? matches.get(0) : null;
        if (user == null || !user.isEnabled() || user.getStatus() != UserStatus.ACTIF) {
            return;
        }
        if (tokenDao.countByUserIdAndCreatedAtAfter(user.getId(), now.minus(Duration.ofHours(1))) >= MAX_LINKS_PER_ACCOUNT_PER_HOUR) {
            return;
        }

        String token = newToken();
        PasswordResetToken reset = new PasswordResetToken();
        reset.setUser(user);
        reset.setTokenHash(hash(token));
        reset.setCreatedAt(now);
        reset.setExpiresAt(now.plus(validity));
        tokenDao.save(reset);

        String to = user.getEmail();
        String name = displayName(user);
        String link = publicUrl + "/reinitialiser-mot-de-passe?token=" + token;
        String username = user.getUsername();
        afterCommit(() -> mailExecutor.execute(() -> sendLink(to, name, username, link)));
    }

    /** Sets the new password if the link is valid, then makes every link of the account unusable. */
    @Transactional
    public void resetPassword(String token, String newPassword) {
        if (newPassword == null || newPassword.length() < MIN_PASSWORD_LENGTH) {
            throw new GlobalException(HttpStatus.BAD_REQUEST,
                    "Le mot de passe doit contenir au moins " + MIN_PASSWORD_LENGTH + " caractères.");
        }
        Instant now = clock.instant();
        PasswordResetToken reset = token == null || token.isBlank() ? null
                : tokenDao.findByTokenHash(hash(token.trim())).orElse(null);
        if (reset == null || reset.getUsedAt() != null || !reset.getExpiresAt().isAfter(now)) {
            throw new GlobalException(HttpStatus.BAD_REQUEST,
                    "Ce lien est invalide ou a expiré. Demandez un nouveau lien.");
        }
        User user = reset.getUser();
        if (!user.isEnabled() || user.getStatus() != UserStatus.ACTIF) {
            throw new GlobalException(HttpStatus.BAD_REQUEST,
                    "Ce compte est désactivé. Contactez l'administrateur.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userDao.save(user);
        tokenDao.deleteByUserId(user.getId());
        tokenDao.deleteUnusable(now);
    }

    private void sendLink(String to, String name, String username, String link) {
        long minutes = validity.toMinutes();
        String subject = "ENSA Labs : réinitialisation de votre mot de passe";
        String text = """
                Bonjour %s,

                Une demande de réinitialisation du mot de passe a été faite pour votre compte ENSA Labs (%s).

                Pour choisir un nouveau mot de passe, ouvrez ce lien (valable %d minutes, une seule fois) :
                %s

                Si vous n'êtes pas à l'origine de cette demande, ignorez ce message : votre mot de passe ne change pas.

                ENSA Labs
                """.formatted(name, username, minutes, link);
        String html = """
                <div style="font-family:Arial,sans-serif;font-size:15px;color:#1f2937;max-width:560px">
                  <p>Bonjour %s,</p>
                  <p>Une demande de réinitialisation du mot de passe a été faite pour votre compte ENSA Labs (<strong>%s</strong>).</p>
                  <p style="margin:24px 0">
                    <a href="%s" style="background:#1e3a8a;color:#ffffff;padding:12px 20px;border-radius:6px;text-decoration:none;display:inline-block">
                      Choisir un nouveau mot de passe
                    </a>
                  </p>
                  <p style="color:#6b7280;font-size:13px">Ce lien est valable %d minutes et ne peut servir qu'une fois.<br>
                  Si le bouton ne marche pas, copiez cette adresse dans votre navigateur :<br>%s</p>
                  <p style="color:#6b7280;font-size:13px">Si vous n'êtes pas à l'origine de cette demande, ignorez ce message : votre mot de passe ne change pas.</p>
                </div>
                """.formatted(HtmlUtils.htmlEscape(name), HtmlUtils.htmlEscape(username),
                HtmlUtils.htmlEscape(link), minutes, HtmlUtils.htmlEscape(link));
        if (!mailService.send(to, subject, text, html)) {
            log.warn("Password reset link could not be e-mailed");
        }
    }

    /** Runs the action once the transaction is committed (right away when there is none). */
    private static void afterCommit(Runnable action) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    action.run();
                }
            });
        } else {
            action.run();
        }
    }

    private boolean allowClient(String clientAddress, Instant now) {
        String key = clientAddress == null || clientAddress.isBlank() ? "unknown" : clientAddress;
        Instant since = now.minus(Duration.ofHours(1));
        Deque<Instant> recent = requestsByClient.computeIfAbsent(key, k -> new ArrayDeque<>());
        synchronized (recent) {
            while (!recent.isEmpty() && recent.peekFirst().isBefore(since)) {
                recent.pollFirst();
            }
            if (recent.size() >= MAX_REQUESTS_PER_CLIENT_PER_HOUR) {
                return false;
            }
            recent.addLast(now);
        }
        if (requestsByClient.size() > 10_000) {
            requestsByClient.entrySet().removeIf(entry -> {
                synchronized (entry.getValue()) {
                    return entry.getValue().isEmpty() || entry.getValue().peekLast().isBefore(since);
                }
            });
        }
        return true;
    }

    private String newToken() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    static String hash(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String displayName(User user) {
        String name = ((user.getFirstName() == null ? "" : user.getFirstName()) + " "
                + (user.getLastName() == null ? "" : user.getLastName())).trim();
        return name.isEmpty() ? user.getUsername() : name;
    }

    @PreDestroy
    void shutdown() {
        mailExecutor.shutdown();
    }
}
