package com.example.demo;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Reteta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "pacient_id")
    private Pacient pacient;

    private LocalDate data;
    private String reteta;
    private String compensata; // DA/NU

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public Pacient getPacient() { return pacient; }
    public void setPacient(Pacient pacient) { this.pacient = pacient; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public String getReteta() { return reteta; }
    public void setReteta(String reteta) { this.reteta = reteta; }

    public String getCompensata() { return compensata; }
    public void setCompensata(String compensata) { this.compensata = compensata; }
}
