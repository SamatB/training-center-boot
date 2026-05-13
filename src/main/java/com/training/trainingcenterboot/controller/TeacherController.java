// controller/TeacherController.java

package com.training.trainingcenterboot.controller;

import com.training.trainingcenterboot.model.Teacher;
import com.training.trainingcenterboot.service.TeacherService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public List<Teacher> getAll() {
        return teacherService.getAll();
    }

    @PostMapping
    public Teacher create(@RequestBody Teacher teacher) {
        return teacherService.create(teacher);
    }

    @GetMapping("/experienced")
    public List<Teacher> getExperiencedTeachers(@RequestParam int experience) {
        return teacherService.getExperiencedTeachers(experience);
    }

    @GetMapping("/sorted")
    public List<Teacher> getSortedTeachers() {
        return teacherService.getSortedTeachers();
    }
}