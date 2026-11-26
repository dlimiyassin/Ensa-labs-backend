package com.ensa.labs.zBase.security.service.impl;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ensa.labs.zBase.security.bean.Role;
import com.ensa.labs.zBase.security.bean.User;
import com.ensa.labs.zBase.security.bean.enums.UserStatus;
import com.ensa.labs.zBase.security.constant.Roles;
import com.ensa.labs.zBase.security.dao.facade.RoleDao;
import com.ensa.labs.zBase.security.dao.facade.UserDao;
import com.ensa.labs.zBase.security.service.facade.AuthService;
import com.ensa.labs.zBase.security.service.facade.UserService;
import com.ensa.labs.zBase.security.utils.JWTHelper;
import com.ensa.labs.zBase.security.ws.dto.JWTAuthResponse;
import com.ensa.labs.zBase.security.ws.dto.LoginDto;
import com.ensa.labs.zBase.security.ws.dto.RegisterDto;
import com.ensa.labs.zBase.testing.TestProfileAccess;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.ensa.labs.zBase.security.constant.JWTUtil.REFRESH_TOKEN_TYPE;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTHelper jwtHelper;
    private final UserService userService;
    private final TestProfileAccess testProfileAccess;

    public AuthServiceImpl(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager, JWTHelper jwtHelper,
                           UserService userService, TestProfileAccess testProfileAccess) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtHelper = jwtHelper;
        this.userService = userService;
        this.testProfileAccess = testProfileAccess;
    }

    @Override
    public JWTAuthResponse login(LoginDto loginDto) {
        Authentication authenticate;
        try {
            String identifier = loginDto.getUsername().strip();
            String username = identifier;
            if (identifier.contains("@")) {
                List<User> matches = userDao.findTop2ByEmailIgnoreCase(identifier);
                if (matches.size() > 1) {
                    throw new BadCredentialsException("Username or Password incorrect");
                }
                if (!matches.isEmpty()) {
                    username = matches.getFirst().getUsername();
                }
            }
            testProfileAccess.requireAccountAccess(username);
            authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, loginDto.getPassword()));
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Username or Password incorrect");
        }
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authenticate.getPrincipal();
        userService.updateLastLogin(user.getUsername());
        return issueTokens(user.getUsername(), user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
    }

    @Override
    @Transactional(readOnly = true)
    public JWTAuthResponse refresh(String refreshToken) {
        String username;
        try {
            username = jwtHelper.verify(refreshToken, REFRESH_TOKEN_TYPE).getSubject();
        } catch (JWTVerificationException e) {
            throw new BadCredentialsException("Invalid or expired refresh token");
        }
        testProfileAccess.requireAccountAccess(username);
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid or expired refresh token"));
        if (!user.isEnabled() || user.getStatus() == UserStatus.BLOQUE) {
            throw new BadCredentialsException("Account disabled");
        }
        return issueTokens(user.getUsername(), user.getRoles().stream().map(Role::getName).toList());
    }

    @Override
    @Transactional
    public String register(RegisterDto registerDto) {
        if (TestProfileAccess.isTestAccount(registerDto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This username is reserved for test profiles");
        }
        if (userDao.findByUsername(registerDto.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        Set<Role> roles = new HashSet<>();
        roles.add(roleDao.findByName(Roles.RESEARCHER).orElseThrow(() -> new IllegalStateException(Roles.RESEARCHER + " role not found")));
        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setPhoneNumber(registerDto.getPhoneNumber());
        user.setEmail(registerDto.getEmail());
        user.setUsername(registerDto.getUsername());
        user.setCin(registerDto.getCin());
        user.setEnabled(true);
        user.setStatus(UserStatus.ACTIF);
        user.setPassword(passwordEncoder.encode(registerDto.getCin()));
        user.setRoles(roles);
        return userDao.save(user).getId();
    }

    private JWTAuthResponse issueTokens(String username, List<String> roles) {
        return new JWTAuthResponse(
                jwtHelper.generateAccessToken(username, roles),
                jwtHelper.generateRefreshToken(username),
                jwtHelper.getAccessTokenExpirationMs() / 1000);
    }
}
