package com.example.task_manager.controller;

import com.example.task_manager.service.AuthService;
import com.example.task_manager.dto.user.JwtRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(CREATED)
    public void register(@RequestBody @Valid JwtRequest jwtRequest){
        authService.register(jwtRequest);
    }

    @PostMapping("/admin-register")
    @ResponseStatus(OK)
    public void adminRegister(@RequestBody @Valid JwtRequest jwtRequest,
                                 @RequestParam String adminCode){
        authService.adminRegister(jwtRequest, adminCode);
    }
}
