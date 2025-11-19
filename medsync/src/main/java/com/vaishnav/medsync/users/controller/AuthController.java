package com.vaishnav.medsync.users.controller;

import com.vaishnav.medsync.users.entity.User;
import com.vaishnav.medsync.users.io.LoginRequestDto;
import com.vaishnav.medsync.users.io.OtpVerificationDto;
import com.vaishnav.medsync.users.io.RegisterRequestDto;
import com.vaishnav.medsync.users.security.jwt.JwtAuthResponse;
import com.vaishnav.medsync.users.service.MailVerificationService;
import com.vaishnav.medsync.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private MailVerificationService mailVerificationService;

    @PostMapping("/public/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@RequestBody RegisterRequestDto requestDto){
        return userService.registerUser(requestDto);
    }

    @PostMapping("/public/login")
    @ResponseStatus(HttpStatus.OK)
    public JwtAuthResponse loginUser(@RequestBody LoginRequestDto loginRequestDto){
        return userService.loginUser(loginRequestDto);
    }

    @PostMapping("/public/verify")
    @ResponseStatus(HttpStatus.OK)
    public String verifyEmailOtp(@RequestBody OtpVerificationDto otpVerificationDto){
        return mailVerificationService.verifyEmailOtp(otpVerificationDto);
    }

    @GetMapping("/public/resend/{email}")
    @ResponseStatus(HttpStatus.OK)
    public String resendEmailOtp(@PathVariable String email){
        return userService.resendOTP(email);
    }

}
