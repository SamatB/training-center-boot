package com.training.trainingcenterboot.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CourseRequest {

    @NotBlank(message = "Название курса обязательно")
    private String title;

    @Min(value = 1, message = "Длительность должна быть минимум 1 месяц")
    private int duration;

    @Min(value = 0, message = "Цена не может быть отрицательной")
    private double price;

    @NotNull(message = "ID преподавателя обязателен")
    @Min(value = 1, message = "ID не может быть отрицательным")
    private Long teacherId;
}