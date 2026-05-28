package com.training.trainingcenterboot.controller.mvc;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardViewController {

    @GetMapping("/mvc/dashboard")
    public String dashboard(Authentication authentication, Model model) {

        if (authentication == null) {
            return "redirect:/mvc/login";
        }

        model.addAttribute("username", authentication.getName());

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        boolean isStudent = authentication.getAuthorities()
                .stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_STUDENT"));

        boolean isTeacher = authentication.getAuthorities()
                .stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_TEACHER"));

        if (isAdmin) {
            return "admin/admin-dashboard";
        }

        if (isStudent) {
            return "student/student-dashboard";
        }

        if (isTeacher) {
            return "teacher/teacher-dashboard";
        }

        return "redirect:/mvc/login";
    }
}