package com.vaishnav.medsync.doctors.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRequestDto {
    private String specialization;
    private String licenseNumber;
    private int yearsOfExperience;
    private String clinicAddress;
    private List<String> qualifications;
    private List<String> availableModes;
    private String bio;
}
