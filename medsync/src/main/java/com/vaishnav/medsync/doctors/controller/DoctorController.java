package com.vaishnav.medsync.doctors.controller;

import com.vaishnav.medsync.doctors.io.DoctorRequestDto;
import com.vaishnav.medsync.doctors.io.DoctorResponseDto;
import com.vaishnav.medsync.doctors.service.DoctorService;
import com.vaishnav.medsync.users.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private JwtUtils jwtUtils;

    private String extractUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        return jwtUtils.getUsernameFromJWT(token);
    }

    @PostMapping("/profile")
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorResponseDto createDoctorProfile(HttpServletRequest request, @RequestBody DoctorRequestDto requestDto){
        String email = extractUserId(request);

        return doctorService.createDoctorProfile(email,requestDto);
    }

    @PutMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public DoctorResponseDto updateDoctorProfile(HttpServletRequest request, @RequestBody DoctorRequestDto requestDto){
        String email = extractUserId(request);

        return doctorService.updateDoctorProfile(email,requestDto);
    }

    @GetMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    public DoctorResponseDto getDoctorByEmail(HttpServletRequest request){
        String email = extractUserId(request);
        return doctorService.getDoctorByEmail(email);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DoctorResponseDto> getDoctors(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "5") int pageSize,
            @RequestParam(required = false, defaultValue = "yearsOfExperience") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) String search
    ){
        Sort sort = null;
        if(sortOrder.equalsIgnoreCase("desc")){
            sort = Sort.by(sortBy).descending();
        }
        else{
            sort = Sort.by(sortBy).ascending();
        }

        // PageNo - 1 because we will give 1 based indexing
        PageRequest pageRequest = PageRequest.of(pageNo-1, pageSize,sort);

        return doctorService.getDoctors(pageRequest,search);
    }

}
