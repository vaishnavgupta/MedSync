package com.vaishnav.medsync.patient.io;

import com.vaishnav.medsync.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponseDto {
    private String userEmail;
    private String bloodGroup;
    private String gender;
    private String medicalHistory;
    private String address;
    private String majorProblem;
    private LocalDate registrationDate;
}
