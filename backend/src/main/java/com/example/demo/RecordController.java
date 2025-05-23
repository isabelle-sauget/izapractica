package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/records")
@CrossOrigin(origins = "*")
public class RecordController {
    @Autowired
    private RecordRepository repository;

    @GetMapping
    public List<Record> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public Record create(@RequestBody Record record) {
        return repository.save(record);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Record> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Record> update(@PathVariable Long id, @RequestBody Record updatedRecord) {
        return repository.findById(id)
                .map(record -> {
                    record.setIndex1(updatedRecord.getIndex1());
                    record.setField1(updatedRecord.getField1());
                    record.setField2(updatedRecord.getField2());
                    record.setField3(updatedRecord.getField3());
                    Record saved = repository.save(record);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
