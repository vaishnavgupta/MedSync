package com.vaishnav.medsync.users.controller;

import com.vaishnav.medsync.users.io.UserResponseDto;
import com.vaishnav.medsync.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);

        UserResponseDto userResponseDto = userService.getUserFromJwt(token);

        return ResponseEntity.ok(userResponseDto);
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateCurrentUser(HttpServletRequest request, @RequestBody UserResponseDto requestDto){
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);

        UserResponseDto responseDto = userService.updateUserFromJwt(token, requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getUserByEmail(@PathVariable String email){
        return userService.getUserByEmail(email);
    }

}
