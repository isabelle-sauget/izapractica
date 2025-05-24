package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/retete")
@CrossOrigin(origins = "*")
public class RetetaController {
    @Autowired
    private RetetaRepository retetaRepository;

    @GetMapping
    public List<Reteta> getAll() {
        return retetaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Reteta getById(@PathVariable Long id) {
        return retetaRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Reteta create(@RequestBody Reteta reteta) {
        return retetaRepository.save(reteta);
    }

    @PutMapping("/{id}")
    public Reteta update(@PathVariable Long id, @RequestBody Reteta reteta) {
        reteta.setId(id);
        return retetaRepository.save(reteta);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        retetaRepository.deleteById(id);
    }
}
