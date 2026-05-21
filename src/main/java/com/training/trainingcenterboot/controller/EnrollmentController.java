package com.training.trainingcenterboot.controller;

import com.training.trainingcenterboot.dto.request.EnrollmentRequest;
import com.training.trainingcenterboot.dto.request.ProgressRequest;
import com.training.trainingcenterboot.dto.response.EnrollmentResponse;
import com.training.trainingcenterboot.dto.response.PageResponse;
import com.training.trainingcenterboot.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
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
    public PageResponse<EnrollmentResponse> getAll(Pageable pageable) {
        return enrollmentService.getAll(pageable);
    }

    @GetMapping("/filter/payment")
    public PageResponse<EnrollmentResponse> filterByPaymentStatus(@RequestParam boolean paid,
                                                                  Pageable pageable) {
        return enrollmentService.filterByPaymentStatus(paid, pageable);
    }

    @GetMapping("/filter/progress")
    public PageResponse<EnrollmentResponse> filterByProgress(@RequestParam int progress,
                                                             Pageable pageable) {
        return enrollmentService.filterByProgress(progress, pageable);
    }

    @GetMapping("/search/course")
    public PageResponse<EnrollmentResponse> searchByCourseTitle(@RequestParam String title,
                                                                Pageable pageable) {
        return enrollmentService.searchByCourseTitle(title, pageable);
    }


//    @GetMapping
//    public List<EnrollmentResponse> getAll() {
//        return enrollmentService.getAll();
//    }

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