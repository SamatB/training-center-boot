package com.training.trainingcenterboot.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ProgressRequest {

    @Min(value = 0, message = "Прогресс не может быть меньше 0")
    @Max(value = 100, message = "Прогресс не может быть больше 100")
    private int progress;
}