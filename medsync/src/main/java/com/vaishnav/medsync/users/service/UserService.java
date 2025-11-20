package com.vaishnav.medsync.users.service;

import com.vaishnav.medsync.users.entity.User;
import com.vaishnav.medsync.users.io.LoginRequestDto;
import com.vaishnav.medsync.users.io.RegisterRequestDto;
import com.vaishnav.medsync.users.io.UserResponseDto;
import com.vaishnav.medsync.users.repository.UserRepository;
import com.vaishnav.medsync.users.security.jwt.JwtAuthResponse;
import com.vaishnav.medsync.users.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final MailVerificationService mailService;

    public User registerUser(RegisterRequestDto requestDto){
        User user =  new User();
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setDob(requestDto.getDob());
        user.setFullName(requestDto.getFullName());
        user.setRole(requestDto.getRole());
        user.setPhone(requestDto.getPhone());
        user.setEmailVerified(false);
        user.setActive(true);

        String otp = generateOTP();
        user.setEmailVerificationOtp(otp);
        user.setOtpExpiry(Timestamp.from(Instant.now().plus(10, ChronoUnit.MINUTES)));

        User retUser =  userRepository.save(user);

        mailService.sendVerificationEmail(retUser.getEmail(), otp);

        return retUser;
    }

    public JwtAuthResponse loginUser(LoginRequestDto requestDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getEmail(),
                        requestDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateJwtToken(principal);
        return new JwtAuthResponse(jwtToken,true);
    }

    public String resendOTP(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found!"));

        if (user.isEmailVerified()){
            return "Email already verified";
        }

        String otp = generateOTP();
        user.setEmailVerificationOtp(otp);
        user.setOtpExpiry(Timestamp.from(Instant.now().plus(10, ChronoUnit.MINUTES)));

        User retUser =  userRepository.save(user);

        mailService.sendVerificationEmail(retUser.getEmail(), otp);

        return "OTP Resend Successfully";
    }

    // Function to generate random OTP
    private String generateOTP(){
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }


    public UserResponseDto getUserFromJwt(String token) {
        String email = jwtUtils.getUsernameFromJWT(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User does not exists"));

        return UserResponseDto.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .dob(user.getDob())
                .phone(user.getPhone())
                .role(user.getRole())
                .build();
    }

    public UserResponseDto updateUserFromJwt(String token, UserResponseDto requestDto) {
        String email = jwtUtils.getUsernameFromJWT(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User does not exists"));

        user.setPhone(requestDto.getPhone());
        user.setFullName(requestDto.getFullName());
        user.setDob(requestDto.getDob());

        User savedUser = userRepository.save(user);

        return UserResponseDto.builder()
                .email(savedUser.getEmail())
                .fullName(savedUser.getFullName())
                .dob(savedUser.getDob())
                .phone(savedUser.getPhone())
                .role(savedUser.getRole())
                .build();
    }

    public UserResponseDto getUserByEmail(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User does not exists"));

        return UserResponseDto.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .dob(user.getDob())
                .phone(user.getPhone())
                .role(user.getRole())
                .build();
    }
}
