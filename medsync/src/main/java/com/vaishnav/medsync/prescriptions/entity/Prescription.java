package com.vaishnav.medsync.prescriptions.entity;

import com.vaishnav.medsync.doctors.entity.Doctor;
import com.vaishnav.medsync.patient.entity.Patient;
import com.vaishnav.medsync.users.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    private LocalDate dateIssued;

    @Column(length = 5000)
    private String extractedText;

    @Column(length = 5000)
    private String medicinesJson;

    private String fileUrl;

    @Column(length = 1000)
    private String notes;

}
