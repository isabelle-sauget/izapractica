package com.example.demo;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Pacient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nume;
    private String prenume;
    private String cnp;
    private String telefon;
    private String adresa;
    private LocalDate dataNasterii;
    private String email;
    @Column(length = 2048)
    private String dateMedicale;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }
    public String getPrenume() { return prenume; }
    public void setPrenume(String prenume) { this.prenume = prenume; }
    public String getCnp() { return cnp; }
    public void setCnp(String cnp) { this.cnp = cnp; }
    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }
    public String getAdresa() { return adresa; }
    public void setAdresa(String adresa) { this.adresa = adresa; }
    public LocalDate getDataNasterii() { return dataNasterii; }
    public void setDataNasterii(LocalDate dataNasterii) { this.dataNasterii = dataNasterii; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDateMedicale() { return dateMedicale; }
    public void setDateMedicale(String dateMedicale) { this.dateMedicale = dateMedicale; }
}
