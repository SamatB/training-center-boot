package com.training.trainingcenterboot.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TeacherRequest {

    @NotBlank(message = "Имя преподавателя обязательно")
    private String name;

    @Min(value = 0, message = "Опыт не может быть отрицательным")
    private int experience;
}