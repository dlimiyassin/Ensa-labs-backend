package com.ensa.labs.zBase.security.service.impl;

import com.ensa.labs.exception.GlobalException;
import com.ensa.labs.zBase.mail.MailService;
import com.ensa.labs.zBase.security.bean.PasswordResetToken;
import com.ensa.labs.zBase.security.bean.User;
import com.ensa.labs.zBase.security.bean.enums.UserStatus;
import com.ensa.labs.zBase.security.dao.facade.PasswordResetTokenDao;
import com.ensa.labs.zBase.security.dao.facade.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** "Forgot password" flow: what is sent, to whom, and which links are accepted. */
class PasswordResetServiceTest {

    private static final Instant NOW = Instant.parse("2026-11-28T10:00:00Z");

    private final UserDao userDao = mock(UserDao.class);
    private final PasswordResetTokenDao tokenDao = mock(PasswordResetTokenDao.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final MailService mailService = mock(MailService.class);
    private PasswordResetService service;
    private User user;

    @BeforeEach
    void setUp() {
        service = new PasswordResetService(userDao, tokenDao, passwordEncoder, mailService,
                "https://labs.example/", Duration.ofMinutes(30), Clock.fixed(NOW, ZoneOffset.UTC));
        user = new User();
        user.setId("u1");
        user.setUsername("dlimi.yassine");
        user.setFirstName("Yassine");
        user.setLastName("Dlimi");
        user.setEmail("Yassine@Example.ma");
        user.setEnabled(true);
        user.setStatus(UserStatus.ACTIF);
        when(mailService.isEnabled()).thenReturn(true);
        when(mailService.send(anyString(), anyString(), anyString(), anyString())).thenReturn(true);
        when(userDao.findTop2ByEmailIgnoreCase(anyString())).thenReturn(List.of());
        when(userDao.findTop2ByEmailIgnoreCase("yassine@example.ma")).thenReturn(List.of(user));
    }

    @Test
    void sendsASingleUseLinkToTheAccountAddress() {
        service.requestReset(" yassine@example.ma ", "10.0.0.1");

        ArgumentCaptor<PasswordResetToken> saved = ArgumentCaptor.forClass(PasswordResetToken.class);
        verify(tokenDao).save(saved.capture());
        assertThat(saved.getValue().getUser()).isSameAs(user);
        assertThat(saved.getValue().getExpiresAt()).isEqualTo(NOW.plus(Duration.ofMinutes(30)));

        ArgumentCaptor<String> text = ArgumentCaptor.forClass(String.class);
        verify(mailService, timeout(2000)).send(eq("Yassine@Example.ma"), anyString(), text.capture(), anyString());
        Matcher link = Pattern.compile("https://labs\\.example/reinitialiser-mot-de-passe\\?token=([A-Za-z0-9_-]+)")
                .matcher(text.getValue());
        assertThat(link.find()).isTrue();
        // Only the hash of the token is stored, never the token itself.
        assertThat(saved.getValue().getTokenHash()).isEqualTo(PasswordResetService.hash(link.group(1)));
        assertThat(saved.getValue().getTokenHash()).isNotEqualTo(link.group(1));
    }

    @Test
    void unknownAddressSendsNothingAndAnswersTheSame() {
        service.requestReset("nobody@example.ma", "10.0.0.1");

        verify(tokenDao, never()).save(any());
        verify(mailService, after(200).never()).send(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void disabledAccountGetsNoLink() {
        user.setEnabled(false);

        service.requestReset("yassine@example.ma", "10.0.0.1");

        verify(tokenDao, never()).save(any());
    }

    @Test
    void nothingHappensWhenMailIsNotConfigured() {
        when(mailService.isEnabled()).thenReturn(false);

        service.requestReset("yassine@example.ma", "10.0.0.1");

        verify(tokenDao, never()).save(any());
    }

    @Test
    void accountGetsAtMostThreeLinksPerHour() {
        when(tokenDao.countByUserIdAndCreatedAtAfter(eq("u1"), any())).thenReturn(3L);

        service.requestReset("yassine@example.ma", "10.0.0.1");

        verify(tokenDao, never()).save(any());
    }

    @Test
    void clientIsLimitedToTenRequestsPerHour() {
        for (int i = 0; i < 10; i++) {
            service.requestReset("nobody@example.ma", "10.0.0.9");
        }
        assertThatThrownBy(() -> service.requestReset("nobody@example.ma", "10.0.0.9"))
                .isInstanceOf(GlobalException.class)
                .extracting("status").isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
        // Another client is not affected.
        service.requestReset("nobody@example.ma", "10.0.0.10");
    }

    @Test
    void validLinkSetsTheNewPasswordAndRemovesTheAccountLinks() {
        PasswordResetToken reset = token(NOW.plus(Duration.ofMinutes(10)), null);
        when(tokenDao.findByTokenHash(PasswordResetService.hash("abc"))).thenReturn(Optional.of(reset));
        when(passwordEncoder.encode("NouveauMdp2026")).thenReturn("encoded");

        service.resetPassword("abc", "NouveauMdp2026");

        assertThat(user.getPassword()).isEqualTo("encoded");
        verify(userDao).save(user);
        verify(tokenDao).deleteByUserId("u1");
    }

    @Test
    void expiredLinkIsRefused() {
        when(tokenDao.findByTokenHash(PasswordResetService.hash("abc")))
                .thenReturn(Optional.of(token(NOW.minus(Duration.ofMinutes(1)), null)));

        assertThatThrownBy(() -> service.resetPassword("abc", "NouveauMdp2026"))
                .isInstanceOf(GlobalException.class)
                .hasMessageContaining("expiré");
        verify(userDao, never()).save(any());
    }

    @Test
    void usedLinkIsRefused() {
        when(tokenDao.findByTokenHash(PasswordResetService.hash("abc")))
                .thenReturn(Optional.of(token(NOW.plus(Duration.ofMinutes(10)), NOW.minus(Duration.ofMinutes(2)))));

        assertThatThrownBy(() -> service.resetPassword("abc", "NouveauMdp2026"))
                .isInstanceOf(GlobalException.class);
        verify(userDao, never()).save(any());
    }

    @Test
    void unknownLinkIsRefused() {
        when(tokenDao.findByTokenHash(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.resetPassword("forged", "NouveauMdp2026"))
                .isInstanceOf(GlobalException.class);
    }

    @Test
    void tooShortPasswordIsRefused() {
        assertThatThrownBy(() -> service.resetPassword("abc", "court"))
                .isInstanceOf(GlobalException.class)
                .hasMessageContaining("8 caractères");
    }

    private PasswordResetToken token(Instant expiresAt, Instant usedAt) {
        PasswordResetToken reset = new PasswordResetToken();
        reset.setUser(user);
        reset.setTokenHash(PasswordResetService.hash("abc"));
        reset.setCreatedAt(NOW.minus(Duration.ofMinutes(5)));
        reset.setExpiresAt(expiresAt);
        reset.setUsedAt(usedAt);
        return reset;
    }
}
