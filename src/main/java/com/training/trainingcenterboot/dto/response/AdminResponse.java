package com.training.trainingcenterboot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminResponse {

    private Long id;
    private String name;
    private String email;
}