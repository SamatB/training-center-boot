package com.training.trainingcenterboot.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TeacherRegisterRequest {

    @NotBlank(message = "Username обязателен")
    private String username;

    @NotBlank(message = "Password обязателен")
    private String password;

    @NotBlank(message = "Имя преподавателя обязательно")
    private String name;

    @Min(value = 0, message = "Опыт не может быть отрицательным")
    @Max(value = 50, message = "Опыт не может быть больше 50 лет")
    private int experience;
}