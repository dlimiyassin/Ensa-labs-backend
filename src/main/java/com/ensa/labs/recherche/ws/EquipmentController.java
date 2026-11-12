package com.ensa.labs.recherche.ws;

import com.ensa.labs.recherche.dto.EquipmentDTO;
import com.ensa.labs.recherche.service.EquipmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipments")
public class EquipmentController {
    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) { this.equipmentService = equipmentService; }

    @GetMapping
    public List<EquipmentDTO> findAll() { return equipmentService.findAll(); }

    @GetMapping("/{id}")
    public EquipmentDTO findById(@PathVariable String id) { return equipmentService.findById(id); }

    @GetMapping("/lab/{labAcronym}")
    public List<EquipmentDTO> findByLabAcronym(@PathVariable String labAcronym) { return equipmentService.findByLabAcronym(labAcronym); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(Authorities.LAB_MANAGERS)
    public EquipmentDTO create(@Valid @RequestBody EquipmentDTO dto) { return equipmentService.create(dto); }

    @PutMapping("/{id}")
    @PreAuthorize(Authorities.LAB_MANAGERS)
    public EquipmentDTO update(@PathVariable String id, @Valid @RequestBody EquipmentDTO dto) { return equipmentService.update(id, dto); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(Authorities.LAB_MANAGERS)
    public void delete(@PathVariable String id) { equipmentService.delete(id); }
}
