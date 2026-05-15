package com.training.trainingcenterboot.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private int duration;
    private double price;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
}