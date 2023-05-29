package com.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "patients_data")
public class PatientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String patient_surname;

    @Column(nullable = false)
    private String patient_name;

    @Column(nullable = false)
    private Integer patient_age;
}
