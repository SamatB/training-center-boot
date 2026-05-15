package com.training.trainingcenterboot.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnrollmentRequest {

    @NotNull(message = "ID студента обязателен")
    private Long studentId;

    @NotNull(message = "ID курса обязателен")
    private Long courseId;
}