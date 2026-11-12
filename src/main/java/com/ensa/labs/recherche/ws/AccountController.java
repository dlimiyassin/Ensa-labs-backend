package com.ensa.labs.recherche.ws;

import com.ensa.labs.recherche.dto.CurrentUserDTO;
import com.ensa.labs.recherche.dto.DashboardStatsDTO;
import com.ensa.labs.recherche.dto.PasswordChangeDTO;
import com.ensa.labs.recherche.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/** The authenticated account: profile and lab scope, password change, dashboard counters. */
@RestController
@RequestMapping("/api/account")
@PreAuthorize("isAuthenticated()")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) { this.accountService = accountService; }

    @GetMapping("/me")
    public CurrentUserDTO me() { return accountService.me(); }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@Valid @RequestBody PasswordChangeDTO dto) { accountService.changePassword(dto); }

    @GetMapping("/stats")
    public DashboardStatsDTO stats(@RequestParam(required = false) String lab) { return accountService.stats(lab); }
}
