package com.vaishnav.medsync.users.service;

import com.vaishnav.medsync.users.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Data
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private Long id;
    private String password;
    private String email;
    private String fullName;
    private String role;
    private String phone;
    private Date dob;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean isEmailVerified;
    private boolean isActive;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Collection<? extends GrantedAuthority> authorities, boolean isActive, boolean isEmailVerified, Timestamp updatedAt, Timestamp createdAt, Date dob, String phone, String role, String fullName, String email, String password, Long id) {
        this.authorities = authorities;
        this.isActive = isActive;
        this.isEmailVerified = isEmailVerified;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.dob = dob;
        this.phone = phone;
        this.role = role;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.id = id;
    }

    // Generating UserDetailsImpl from User
    public static UserDetailsImpl build(User user){
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
        return new UserDetailsImpl(
                Collections.singletonList(authority),
                user.isActive(),
                user.isEmailVerified(),
                user.getUpdatedAt(),
                user.getCreatedAt(),
                user.getDob(),
                user.getPhone(),
                user.getRole(),
                user.getFullName(),
                user.getEmail(),
                user.getPassword(),
                user.getId()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;                   //Considering email as username
    }
}
