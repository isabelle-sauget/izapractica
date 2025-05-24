package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/pacienti")
@CrossOrigin(origins = "*")
public class PacientController {
    @Autowired
    private PacientRepository repository;

    @GetMapping
    public List<Pacient> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public Pacient create(@RequestBody Pacient pacient) {
        return repository.save(pacient);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pacient> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pacient> update(@PathVariable Long id, @RequestBody Pacient updatedPacient) {
        return repository.findById(id)
                .map(pacient -> {
                    pacient.setNume(updatedPacient.getNume());
                    pacient.setPrenume(updatedPacient.getPrenume());
                    pacient.setCnp(updatedPacient.getCnp());
                    pacient.setTelefon(updatedPacient.getTelefon());
                    pacient.setAdresa(updatedPacient.getAdresa());
                    pacient.setDataNasterii(updatedPacient.getDataNasterii());
                    pacient.setEmail(updatedPacient.getEmail());
                    pacient.setDateMedicale(updatedPacient.getDateMedicale());
                    Pacient saved = repository.save(pacient);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
