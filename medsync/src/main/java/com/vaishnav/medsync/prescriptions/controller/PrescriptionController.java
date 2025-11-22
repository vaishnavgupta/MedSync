package com.vaishnav.medsync.prescriptions.controller;

import com.vaishnav.medsync.prescriptions.entity.Prescription;
import com.vaishnav.medsync.prescriptions.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {
    private final PrescriptionService prescriptionService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPrescription(
            @RequestParam("file") MultipartFile file,
            @RequestParam("doctorEmail") String doctorEmail,
            @RequestParam("patientEmail") String patientEmail,
            @RequestParam("notes") String notes
            ){
        try {
            Prescription pres = prescriptionService.uploadAndProcess(file,patientEmail,doctorEmail,notes);
            return ResponseEntity.ok(pres);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to process file: " + e.getMessage());
        } catch (RuntimeException re) {
            return ResponseEntity.badRequest().body(re.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body("Ocr Service Error");
        }
    }

    @GetMapping("/patient/{patientEmail}")
    @ResponseStatus(HttpStatus.OK)
    public List<Prescription> getByPatient(String patientEmail){
        return prescriptionService.getByPatientEmail(patientEmail);
    }

    @GetMapping("/doctor/{doctorEmail}")
    @ResponseStatus(HttpStatus.OK)
    public List<Prescription> getByDoctor(String doctorEmail){
        return prescriptionService.getByDoctorEmail(doctorEmail);
    }
}
