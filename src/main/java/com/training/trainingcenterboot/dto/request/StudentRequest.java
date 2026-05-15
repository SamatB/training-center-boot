package com.training.trainingcenterboot.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class StudentRequest {

    @NotBlank(message = "Имя студента не должно быть пустым")
    private String name;

    @Min(value = 10, message = "Возраст должен быть минимум 10")
    @Max(value = 80, message = "Возраст должен быть максимум 80")
    private int age;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный email")
    private String email;
}