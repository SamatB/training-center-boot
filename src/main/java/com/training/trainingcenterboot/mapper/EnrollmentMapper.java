package com.training.trainingcenterboot.mapper;

import com.training.trainingcenterboot.dto.response.EnrollmentResponse;
import com.training.trainingcenterboot.model.Enrollment;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentMapper {

    public EnrollmentResponse toResponse(Enrollment enrollment) {
        return new EnrollmentResponse(
                enrollment.getId(),
                enrollment.getStudent().getId(),
                enrollment.getStudent().getName(),
                enrollment.getCourse().getId(),
                enrollment.getCourse().getTitle(),
                enrollment.getEnrolledAt(),
                enrollment.getProgress(),
                enrollment.isPaymentStatus()
        );
    }
}