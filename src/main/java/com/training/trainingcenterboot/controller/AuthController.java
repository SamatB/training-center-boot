package com.training.trainingcenterboot.controller;

import com.training.trainingcenterboot.dto.request.StudentRequest;
import com.training.trainingcenterboot.dto.request.TeacherRequest;
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
            @Valid @RequestBody StudentRequest request
    ) {
        return authService.registerStudent(request);
    }

    @PostMapping("/register/teacher")
    public String registerTeacher(
            @Valid @RequestBody TeacherRequest request
    ) {
        return authService.registerTeacher(request);
    }
}