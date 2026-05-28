package com.training.trainingcenterboot.controller.mvc;

import com.training.trainingcenterboot.dto.request.AdminRegisterRequest;
import com.training.trainingcenterboot.dto.request.StudentRegisterRequest;
import com.training.trainingcenterboot.dto.request.TeacherRegisterRequest;
import com.training.trainingcenterboot.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mvc")
public class AuthViewController {

    private final AuthService authService;

    public AuthViewController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/student")
    public String studentRegisterPage(Model model) {
        model.addAttribute("student", new StudentRegisterRequest());
        return "auth/register-student";
    }

    @PostMapping("/register/student")
    public String registerStudent(@Valid @ModelAttribute("student") StudentRegisterRequest request) throws BindException {
        authService.registerStudent(request);
        return "redirect:/mvc/login";
    }

    @GetMapping("/teacher")
    public String teacherRegisterPage(Model model) {
        model.addAttribute("teacher", new TeacherRegisterRequest());
        return "auth/register-teacher";
    }

    @PostMapping("/register/teacher")
    public String registerTeacher(@Valid @ModelAttribute("teacher") TeacherRegisterRequest request) throws BindException {
        authService.registerTeacher(request);
        return "redirect:/mvc/login";
    }

    @GetMapping("/admin")
    public String adminRegisterPage(Model model) {
        model.addAttribute("admin", new AdminRegisterRequest());
        return "auth/register-admin";
    }

    @PostMapping("/register/admin")
    public String registerAdmin(@Valid @ModelAttribute("admin") AdminRegisterRequest request) throws BindException {
        authService.registerAdmin(request);
        return "redirect:/mvc/login";
    }
}