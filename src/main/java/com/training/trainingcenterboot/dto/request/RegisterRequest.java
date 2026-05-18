package com.training.trainingcenterboot.dto.request;

import com.training.trainingcenterboot.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Username обязателен")
    private String username;

    @NotBlank(message = "Password обязателен")
    private String password;

    @NotNull(message = "Role обязательна")
    private Role role;
}