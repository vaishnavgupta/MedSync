package com.vaishnav.medsync.users.io;

import lombok.Data;

import java.util.Date;

@Data
public class RegisterRequestDto {
    private String email;
    private String password;
    private String fullName;
    private String role;
    private Date dob;
    private String phone;
}
