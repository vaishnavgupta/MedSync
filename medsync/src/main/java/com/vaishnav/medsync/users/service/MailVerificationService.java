package com.vaishnav.medsync.users.service;

import com.vaishnav.medsync.users.entity.User;
import com.vaishnav.medsync.users.io.OtpVerificationDto;
import com.vaishnav.medsync.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class MailVerificationService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserRepository userRepository;

    public void sendVerificationEmail(String to, String otp){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setFrom("continumm.films2310@gmail.com");
        msg.setSubject("Verify your MedSync Account");
        msg.setText("Your OTP for email verification is: " + otp + "\nThis OTP is valid for 10 minutes.");
        javaMailSender.send(msg);
    }

    public String verifyEmailOtp(OtpVerificationDto otpDto){
        User user = userRepository.findByEmail(otpDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User Not Found!"));

        if(user.getEmailVerificationOtp() == null){
            return "OTP not generated yet";
        }

        if(user.getOtpExpiry().before(new Timestamp(System.currentTimeMillis()))){
            return "OTP expired. Please request a new OTP.";
        }

        if (!user.getEmailVerificationOtp().equals(otpDto.getOtp())) {
            return "Invalid OTP";
        }

        //OTP Verified
        user.setEmailVerified(true);
        user.setEmailVerificationOtp(null);
        user.setOtpExpiry(null);

        userRepository.save(user);

        return "Email OTP Verified";
    }


}
