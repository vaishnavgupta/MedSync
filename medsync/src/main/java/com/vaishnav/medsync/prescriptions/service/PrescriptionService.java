package com.vaishnav.medsync.prescriptions.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaishnav.medsync.doctors.entity.Doctor;
import com.vaishnav.medsync.doctors.repository.DoctorRepository;
import com.vaishnav.medsync.patient.entity.Patient;
import com.vaishnav.medsync.patient.repository.PatientRepository;
import com.vaishnav.medsync.prescriptions.entity.Prescription;
import com.vaishnav.medsync.prescriptions.repository.PrescriptionRepository;
import com.vaishnav.medsync.users.entity.User;
import com.vaishnav.medsync.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PrescriptionService {


    private final Cloudinary cloudinary;
    private final PrescriptionRepository prescriptionRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("${cloudinary.prescriptions_folder:medsync/prescriptions}")
    private String prescriptionsFolder;

    @Transactional
    public Prescription uploadAndProcess(MultipartFile file, String patientEmail, String doctorEmail, String notes) throws Exception {
        // finding Patient & Doctor
        User patientUser = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new RuntimeException("Patient User does not exist"));
        User doctorUser = userRepository.findByEmail(doctorEmail)
                .orElseThrow(() -> new RuntimeException("Doctor User does not exist"));

        Patient patient = patientRepository.findByUserId(patientUser.getId())
                .orElseThrow(() -> new RuntimeException("Patient details does not exist"));
        Doctor doctor = doctorRepository.findByUserId(doctorUser.getId())
                .orElseThrow(() -> new RuntimeException("Doctor details does not exist"));

        // Upload to Cloudinary
        Map uploadResult;
        if(file!=null && !file.isEmpty()){
            uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                       "folder" , prescriptionsFolder,
                            "resource_type" , "auto"
                    ));
        }
        else {
            throw new RuntimeException("No file provided");
        }

        String fileUrl = (String) uploadResult.get("secure_url");


        //Prescriptions Entity
        Prescription pres = new Prescription();
        pres.setPatient(patient);
        pres.setDoctor(doctor);
        pres.setDateIssued(LocalDate.now());
        pres.setExtractedText(null);
        pres.setMedicinesJson(null);
        pres.setFileUrl(fileUrl);
        pres.setNotes(notes);

        return prescriptionRepository.save(pres);
    }


    public List<Prescription> getByPatientEmail(String patientEmail) {
        User patientUser = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new RuntimeException("Patient User does not exist"));
        return prescriptionRepository.findByPatientId(patientUser.getId());
    }

    public List<Prescription> getByDoctorEmail(String doctorEmail) {
        User doctorUser = userRepository.findByEmail(doctorEmail)
                .orElseThrow(() -> new RuntimeException("Doctor User does not exist"));
        return prescriptionRepository.findByDoctorId(doctorUser.getId());
    }
}
