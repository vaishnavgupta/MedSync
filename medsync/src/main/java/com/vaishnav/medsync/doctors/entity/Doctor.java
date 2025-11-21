package com.vaishnav.medsync.doctors.entity;

import com.vaishnav.medsync.users.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String specialization;
    private String licenseNumber;
    private int yearsOfExperience;
    private String clinicAddress;
    private List<String> qualifications;
    private List<String> availableModes;
    private String bio;
    private LocalDate registrationDate;
}
