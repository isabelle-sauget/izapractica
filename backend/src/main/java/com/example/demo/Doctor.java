package com.example.demo;

import jakarta.persistence.*;

@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nume;
    private String prenume;
    private String cnp;
    private String adresa;
    private String telefon;
    private String email;
    private String specializare;
    private String clinica;
    @Column(length = 2048)
    private String alteInformatii;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }
    public String getPrenume() { return prenume; }
    public void setPrenume(String prenume) { this.prenume = prenume; }
    public String getCnp() { return cnp; }
    public void setCnp(String cnp) { this.cnp = cnp; }
    public String getAdresa() { return adresa; }
    public void setAdresa(String adresa) { this.adresa = adresa; }
    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSpecializare() { return specializare; }
    public void setSpecializare(String specializare) { this.specializare = specializare; }
    public String getClinica() { return clinica; }
    public void setClinica(String clinica) { this.clinica = clinica; }
    public String getAlteInformatii() { return alteInformatii; }
    public void setAlteInformatii(String alteInformatii) { this.alteInformatii = alteInformatii; }
}
