package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/doctori")
@CrossOrigin(origins = "*")
public class DoctorController {
    @Autowired
    private DoctorRepository repository;

    @GetMapping
    public List<Doctor> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public Doctor create(@RequestBody Doctor doctor) {
        return repository.save(doctor);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> update(@PathVariable Long id, @RequestBody Doctor updatedDoctor) {
        return repository.findById(id)
                .map(doctor -> {
                    doctor.setNume(updatedDoctor.getNume());
                    doctor.setPrenume(updatedDoctor.getPrenume());
                    doctor.setCnp(updatedDoctor.getCnp());
                    doctor.setAdresa(updatedDoctor.getAdresa());
                    doctor.setTelefon(updatedDoctor.getTelefon());
                    doctor.setEmail(updatedDoctor.getEmail());
                    doctor.setSpecializare(updatedDoctor.getSpecializare());
                    doctor.setClinica(updatedDoctor.getClinica());
                    doctor.setAlteInformatii(updatedDoctor.getAlteInformatii());
                    Doctor saved = repository.save(doctor);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
