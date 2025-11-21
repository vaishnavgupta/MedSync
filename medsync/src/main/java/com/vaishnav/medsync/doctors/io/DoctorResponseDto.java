package com.vaishnav.medsync.doctors.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponseDto {
    private String userEmail;
    private String specialization;
    private String licenseNumber;
    private int yearsOfExperience;
    private String clinicAddress;
    private List<String> qualifications;
    private List<String> availableModes;
    private String bio;
    private LocalDate registrationDate;
}
