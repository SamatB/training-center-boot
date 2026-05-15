package com.training.trainingcenterboot.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {

    private Long id;
    private String title;
    private int duration;
    private double price;

    private Long teacherId;
    private String teacherName;
}