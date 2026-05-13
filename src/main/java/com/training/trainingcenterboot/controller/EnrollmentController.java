// controller/EnrollmentController.java

package com.training.trainingcenterboot.controller;

import com.training.trainingcenterboot.model.Enrollment;
import com.training.trainingcenterboot.service.EnrollmentService;
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
    public List<Enrollment> getAll() {
        return enrollmentService.getAll();
    }

    @PostMapping
    public Enrollment enroll(@RequestParam Long studentId,
                             @RequestParam Long courseId) {
        return enrollmentService.enroll(studentId, courseId);
    }

    @GetMapping("/payment-status")
    public List<Enrollment> getByPaymentStatus(@RequestParam boolean status) {
        return enrollmentService.getByPaymentStatus(status);
    }

    @GetMapping("/successful")
    public List<Enrollment> getSuccessfulStudents(@RequestParam int progress) {
        return enrollmentService.getSuccessfulStudents(progress);
    }

    @GetMapping("/unpaid")
    public List<Enrollment> getUnpaidStudents() {
        return enrollmentService.getUnpaidStudents();
    }

    @GetMapping("/course")
    public List<Enrollment> getByCourseName(@RequestParam String title) {
        return enrollmentService.getByCourseName(title);
    }
}