package com.training.trainingcenterboot.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class StudentRegisterRequest {

    @NotBlank(message = "Username обязателен")
    private String username;

    @NotBlank(message = "Password обязателен")
    private String password;

    @NotBlank(message = "Имя студента не должно быть пустым")
    @Size(max = 50, message = "Длина имени не должна превышать 50 символов")
    private String name;

    @Min(value = 10, message = "Возраст должен быть минимум 10")
    @Max(value = 80, message = "Возраст должен быть максимум 80")
    private int age;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный email")
    private String email;
}