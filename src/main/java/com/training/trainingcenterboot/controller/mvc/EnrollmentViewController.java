package com.training.trainingcenterboot.controller.mvc;

import com.training.trainingcenterboot.dto.request.EnrollmentRequest;
import com.training.trainingcenterboot.dto.request.ProgressRequest;
import com.training.trainingcenterboot.service.EnrollmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EnrollmentViewController {

    private final EnrollmentService enrollmentService;

    public EnrollmentViewController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/mvc/enrollments")
    public String enrollmentsPage(Model model) {
        model.addAttribute("enrollments", enrollmentService.getAll());
        return "enrollment/enrollments";
    }

    @GetMapping("/mvc/enrollments/create")
    public String createEnrollmentPage(Model model) {
        model.addAttribute("enrollment", new EnrollmentRequest());
        return "enrollment/enrollment-create";
    }

    @PostMapping("/mvc/enrollments/create")
    public String createEnrollment(@ModelAttribute("enrollment") EnrollmentRequest request) {
        enrollmentService.enroll(request);
        return "redirect:/mvc/enrollments";
    }

    @PostMapping("/mvc/enrollments/pay/{id}")
    public String payEnrollment(@PathVariable Long id) {
        enrollmentService.pay(id);
        return "redirect:/mvc/enrollments";
    }

    @GetMapping("/mvc/enrollments/progress/{id}")
    public String progressPage(@PathVariable Long id, Model model) {
        model.addAttribute("enrollmentId", id);
        model.addAttribute("progressRequest", new ProgressRequest());
        return "enrollment/enrollment-progress";
    }

    @PostMapping("/mvc/enrollments/progress/{id}")
    public String updateProgress(@PathVariable Long id,
                                 @ModelAttribute("progressRequest") ProgressRequest request) {
        enrollmentService.updateProgress(id, request);
        return "redirect:/mvc/enrollments";
    }
}