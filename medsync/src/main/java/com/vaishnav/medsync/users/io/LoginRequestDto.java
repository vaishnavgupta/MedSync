package com.vaishnav.medsync.users.io;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;           //username == email
    private String password;
}
