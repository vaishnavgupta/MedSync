package com.vaishnav.medsync.patient.controller;

import com.vaishnav.medsync.patient.io.PatientRequestDto;
import com.vaishnav.medsync.patient.io.PatientResponseDto;
import com.vaishnav.medsync.patient.service.PatientService;
import com.vaishnav.medsync.users.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private JwtUtils jwtUtils;

    private String extractUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        return jwtUtils.getUsernameFromJWT(token);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public PatientResponseDto createPatient(@RequestBody PatientRequestDto patientRequestDto, HttpServletRequest request){
        String email = extractUserId(request);
        return patientService.createPatient(email,patientRequestDto);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public PatientResponseDto updatePatient(@RequestBody PatientRequestDto patientRequestDto, HttpServletRequest request){
        String email = extractUserId(request);
        return patientService.updatePatient(email,patientRequestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PatientResponseDto getPatient(HttpServletRequest request){
        String email = extractUserId(request);
        return patientService.getPatient(email);
    }

    @GetMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    public PatientResponseDto getPatientById(@PathVariable String email){
        return patientService.getPatient(email);
    }

}
