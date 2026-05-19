package com.training.trainingcenterboot.controller;

import com.training.trainingcenterboot.dto.request.LoginRequest;
import com.training.trainingcenterboot.dto.request.StudentRegisterRequest;
import com.training.trainingcenterboot.dto.request.TeacherRegisterRequest;
import com.training.trainingcenterboot.dto.response.AuthResponse;
import com.training.trainingcenterboot.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register/student")
    public String registerStudent(
            @Valid @RequestBody StudentRegisterRequest request
    ) {
        return authService.registerStudent(request);
    }

    @PostMapping("/register/teacher")
    public String registerTeacher(
            @Valid @RequestBody TeacherRegisterRequest request
    ) {
        return authService.registerTeacher(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}