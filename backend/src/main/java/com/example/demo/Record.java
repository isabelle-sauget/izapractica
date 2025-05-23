package com.example.demo;

import jakarta.persistence.*;

@Entity
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String index1;
    private String field1;
    private String field2;
    private String field3;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIndex1() { return index1; }
    public void setIndex1(String index1) { this.index1 = index1; }
    public String getField1() { return field1; }
    public void setField1(String field1) { this.field1 = field1; }
    public String getField2() { return field2; }
    public void setField2(String field2) { this.field2 = field2; }
    public String getField3() { return field3; }
    public void setField3(String field3) { this.field3 = field3; }
}
