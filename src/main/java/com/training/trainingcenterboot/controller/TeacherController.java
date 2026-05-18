package com.training.trainingcenterboot.controller;

import com.training.trainingcenterboot.dto.request.TeacherRequest;
import com.training.trainingcenterboot.dto.response.TeacherResponse;
import com.training.trainingcenterboot.service.TeacherService;
import jakarta.validation.Valid;
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
    public List<TeacherResponse> getAll() {
        return teacherService.getAll();
    }

//    @PostMapping
//    public TeacherResponse create(@Valid @RequestBody TeacherRequest request) {
//        return teacherService.create(request);
//    }

    @GetMapping("/experienced")
    public List<TeacherResponse> experienced(@RequestParam int experience) {
        return teacherService.getExperiencedTeachers(experience);
    }
}