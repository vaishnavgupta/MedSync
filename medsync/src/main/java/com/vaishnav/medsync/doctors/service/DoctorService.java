package com.vaishnav.medsync.doctors.service;

import com.vaishnav.medsync.doctors.entity.Doctor;
import com.vaishnav.medsync.doctors.io.DoctorRequestDto;
import com.vaishnav.medsync.doctors.io.DoctorResponseDto;
import com.vaishnav.medsync.doctors.repository.DoctorRepository;
import com.vaishnav.medsync.users.entity.User;
import com.vaishnav.medsync.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    public DoctorResponseDto createDoctorProfile(String email, DoctorRequestDto requestDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setSpecialization(requestDto.getSpecialization());
        doctor.setLicenseNumber(requestDto.getLicenseNumber());
        doctor.setClinicAddress(requestDto.getClinicAddress());
        doctor.setYearsOfExperience(requestDto.getYearsOfExperience());
        doctor.setQualifications(requestDto.getQualifications());
        doctor.setAvailableModes(requestDto.getAvailableModes());
        doctor.setBio(requestDto.getBio());
        doctor.setRegistrationDate(LocalDate.now());

        Doctor savedDoctor = doctorRepository.save(doctor);
        return convertToDoctorResponse(savedDoctor);
    }

    public DoctorResponseDto updateDoctorProfile(String email, DoctorRequestDto requestDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Doctor existing = doctorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        existing.setSpecialization(requestDto.getSpecialization());
        existing.setLicenseNumber(requestDto.getLicenseNumber());
        existing.setClinicAddress(requestDto.getClinicAddress());
        existing.setYearsOfExperience(requestDto.getYearsOfExperience());
        existing.setQualifications(requestDto.getQualifications());
        existing.setAvailableModes(requestDto.getAvailableModes());
        existing.setBio(requestDto.getBio());

        Doctor savedDoctor = doctorRepository.save(existing);

        return convertToDoctorResponse(savedDoctor);
    }

    public DoctorResponseDto getDoctorByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Doctor existing = doctorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        return convertToDoctorResponse(existing);
    }

    private DoctorResponseDto convertToDoctorResponse(Doctor doctor){
        return DoctorResponseDto.builder()
                .userEmail(doctor.getUser().getEmail())
                .specialization(doctor.getSpecialization())
                .licenseNumber(doctor.getLicenseNumber())
                .yearsOfExperience(doctor.getYearsOfExperience())
                .clinicAddress(doctor.getClinicAddress())
                .qualifications(doctor.getQualifications())
                .availableModes(doctor.getAvailableModes())
                .bio(doctor.getBio())
                .registrationDate(doctor.getRegistrationDate())
                .build();
    }


    // SQL --> SELECT * FROM doctor limit 5 offset 10
    public List<DoctorResponseDto> getDoctors(Pageable pageable, String search) {
        if(search == null){
            return doctorRepository.findAll(pageable).getContent()
                    .stream()
                    .map(doctor -> convertToDoctorResponse(doctor))
                    .collect(Collectors.toList());
        }
        else{
            return doctorRepository.findBySpecialization(search,pageable).getContent()
                    .stream()
                    .map(doctor -> convertToDoctorResponse(doctor))
                    .collect(Collectors.toList());
        }
    }
}
