package com.training.trainingcenterboot.controller;

import com.training.trainingcenterboot.dto.request.EnrollmentRequest;
import com.training.trainingcenterboot.dto.request.ProgressRequest;
import com.training.trainingcenterboot.dto.response.EnrollmentResponse;
import com.training.trainingcenterboot.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public List<EnrollmentResponse> getAll() {
        return enrollmentService.getAll();
    }

    @PostMapping
    public EnrollmentResponse enroll(@Valid @RequestBody EnrollmentRequest request) {
        return enrollmentService.enroll(request);
    }

    @PatchMapping("/{id}/pay")
    public EnrollmentResponse pay(@PathVariable Long id) {
        return enrollmentService.pay(id);
    }

    @PatchMapping("/{id}/progress")
    public EnrollmentResponse updateProgress(@PathVariable Long id,
                                             @Valid @RequestBody ProgressRequest request) {
        return enrollmentService.updateProgress(id, request);
    }

    @GetMapping("/paid")
    public List<EnrollmentResponse> paidStudents() {
        return enrollmentService.getPaidStudents();
    }

    @GetMapping("/successful")
    public List<EnrollmentResponse> successfulStudents(@RequestParam int progress) {
        return enrollmentService.getSuccessfulStudents(progress);
    }

    @GetMapping("/course")
    public List<EnrollmentResponse> byCourseTitle(@RequestParam String title) {
        return enrollmentService.getByCourseTitle(title);
    }
}