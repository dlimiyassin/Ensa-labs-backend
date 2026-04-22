package com.ensa.labs.recherche.ws;

import com.ensa.labs.recherche.dto.ProductionDTO;
import com.ensa.labs.recherche.service.ProductionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productions")
public class ProductionController {
    private final ProductionService productionService;

    public ProductionController(ProductionService productionService) {
        this.productionService = productionService;
    }

    @GetMapping
    public List<ProductionDTO> findAll() { return productionService.findAll(); }

    @GetMapping("/{id}")
    public ProductionDTO findById(@PathVariable String id) { return productionService.findById(id); }

    @GetMapping("/lab/{labAcronym}")
    public ProductionDTO findByLabAcronym(@PathVariable String labAcronym) { return productionService.findByLabAcronym(labAcronym); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductionDTO create(@RequestBody ProductionDTO dto) { return productionService.create(dto); }

    @PutMapping("/{id}")
    public ProductionDTO update(@PathVariable String id, @RequestBody ProductionDTO dto) { return productionService.update(id, dto); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) { productionService.delete(id); }
}
