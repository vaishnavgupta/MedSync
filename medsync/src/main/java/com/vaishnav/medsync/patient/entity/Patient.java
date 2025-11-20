package com.vaishnav.medsync.patient.entity;

import com.vaishnav.medsync.users.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String bloodGroup;
    private String gender;
    private String medicalHistory;
    private String address;
    private String majorProblem;

    private LocalDate registrationDate;
}
