package com.vaishnav.medsync.patient.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientRequestDto {
    private String bloodGroup;
    private String gender;
    private String medicalHistory;
    private String address;
    private String majorProblem;
}
