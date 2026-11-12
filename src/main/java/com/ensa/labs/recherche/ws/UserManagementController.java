package com.ensa.labs.recherche.ws;

import com.ensa.labs.recherche.dto.UserDTO;
import com.ensa.labs.recherche.service.UserManagementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@PreAuthorize(Authorities.SUPER_ADMIN)
public class UserManagementController {
    private final UserManagementService userManagementService;

    public UserManagementController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @GetMapping
    public List<UserDTO> findAll() { return userManagementService.findAll(); }

    @GetMapping("/{id}")
    public UserDTO findById(@PathVariable String id) { return userManagementService.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody UserDTO dto) { return userManagementService.create(dto); }

    @PutMapping("/{id}")
    public UserDTO update(@PathVariable String id, @Valid @RequestBody UserDTO dto) { return userManagementService.update(id, dto); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) { userManagementService.delete(id); }
}
