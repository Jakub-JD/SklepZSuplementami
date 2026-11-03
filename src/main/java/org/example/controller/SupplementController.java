package org.example.controller;

import org.example.model.Supplement;
import org.example.repository.SupplementRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Klasa kontrolera REST dla encji Supplement w domenie systemu.
 *
 * Udostępnia interfejs sieciowy API (pod adresem /api/supplements)
 * do obsługioperacji CRUD.
 */

@RestController
@RequestMapping("/api/supplements")
public class SupplementController {

    private final SupplementRepository repository;

    public SupplementController(SupplementRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Supplement addSupplement(@RequestBody Supplement supplement) {
        return repository.save(supplement);
    }

    @GetMapping
    public List<Supplement> getAllSupplements() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Supplement getSupplementById(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono suplementu o ID: " + id));
    }

    @PutMapping("/{id}")
    public Supplement updateSupplement(@PathVariable Long id, @RequestBody Supplement updatedSupplement) {
        return repository.findById(id).map(supplement -> {
            supplement.setName(updatedSupplement.getName());
            supplement.setCategory(updatedSupplement.getCategory());
            supplement.setPrice(updatedSupplement.getPrice());
            return repository.save(supplement);
        }).orElseThrow(() -> new RuntimeException("Nie znaleziono suplementu o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteSupplement(@PathVariable Long id) {
        repository.deleteById(id);
    }
}