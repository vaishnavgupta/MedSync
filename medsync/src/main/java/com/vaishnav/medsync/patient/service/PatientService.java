package com.vaishnav.medsync.patient.service;

import com.vaishnav.medsync.patient.entity.Patient;
import com.vaishnav.medsync.patient.io.PatientRequestDto;
import com.vaishnav.medsync.patient.io.PatientResponseDto;
import com.vaishnav.medsync.patient.repository.PatientRepository;
import com.vaishnav.medsync.users.entity.User;
import com.vaishnav.medsync.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public PatientResponseDto createPatient(String email, PatientRequestDto patientRequestDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Patient patient = new Patient();
        patient.setUser(user);
        patient.setBloodGroup(patientRequestDto.getBloodGroup());
        patient.setGender(patientRequestDto.getGender());
        patient.setMedicalHistory(patientRequestDto.getMedicalHistory());
        patient.setAddress(patientRequestDto.getAddress());
        patient.setMajorProblem(patientRequestDto.getMajorProblem());
        patient.setRegistrationDate(LocalDate.now());

        Patient savedPatient = patientRepository.save(patient);

        return convertToPatientResponse(savedPatient);
    }

    public PatientResponseDto updatePatient(String email, PatientRequestDto patientRequestDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Patient existing = patientRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Patient details does not exists"));

        existing.setBloodGroup(patientRequestDto.getBloodGroup());
        existing.setGender(patientRequestDto.getGender());
        existing.setMedicalHistory(patientRequestDto.getMedicalHistory());
        existing.setAddress(patientRequestDto.getAddress());
        existing.setMajorProblem(patientRequestDto.getMajorProblem());

        Patient saved = patientRepository.save(existing);
        return convertToPatientResponse(saved);
    }

    private PatientResponseDto convertToPatientResponse(Patient savedPatient) {
        return PatientResponseDto.builder()
                .userEmail(savedPatient.getUser().getEmail())
                .bloodGroup(savedPatient.getBloodGroup())
                .gender(savedPatient.getGender())
                .medicalHistory(savedPatient.getMedicalHistory())
                .address(savedPatient.getAddress())
                .majorProblem(savedPatient.getMajorProblem())
                .registrationDate(savedPatient.getRegistrationDate())
                .build();
    }

    public PatientResponseDto getPatient(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Patient existing = patientRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Patient details does not exists"));

        return convertToPatientResponse(existing);
    }
}
