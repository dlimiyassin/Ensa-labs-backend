package com.ensa.labs.zBase.testing;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/test-profiles")
public class TestProfileController {
    private final TestProfiles profiles;
    private final TestProfileAccess access;

    public TestProfileController(TestProfiles profiles, TestProfileAccess access) {
        this.profiles = profiles;
        this.access = access;
    }

    @GetMapping
    public ResponseEntity<List<TestProfiles.ProfileOption>> options(HttpServletRequest request) {
        return ResponseEntity.ok().cacheControl(CacheControl.noStore())
                .body(access.hasAccess(request) ? profiles.options() : List.of());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestProfiles.Credentials> credentials(@PathVariable String id, HttpServletRequest request) {
        if (!access.hasAccess(request)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        var selected = profiles.randomCredentials(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(selected);
    }
}
