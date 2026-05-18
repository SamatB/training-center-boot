package com.training.trainingcenterboot.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnrollmentRequest {

    @NotNull(message = "ID студента обязателен")
    @Min(value = 1, message = "ID не может быть отрицательным")
    private Long studentId;

    @NotNull(message = "ID курса обязателен")
    @Min(value = 1, message = "ID не может быть отрицательным")
    private Long courseId;
}