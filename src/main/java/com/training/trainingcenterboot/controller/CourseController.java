package com.training.trainingcenterboot.controller;

import com.training.trainingcenterboot.dto.request.CourseRequest;
import com.training.trainingcenterboot.dto.response.CourseResponse;
import com.training.trainingcenterboot.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<CourseResponse> getAll() {
        return courseService.getAll();
    }

    @GetMapping("/{id}")
    public CourseResponse getById(@PathVariable Long id) {
        return courseService.getById(id);
    }

    @PostMapping
    public CourseResponse create(@Valid @RequestBody CourseRequest request) {
        return courseService.create(request);
    }

    @GetMapping("/cheap")
    public List<CourseResponse> cheap(@RequestParam double price) {
        return courseService.getCheapCourses(price);
    }

    @GetMapping("/long")
    public List<CourseResponse> longCourses(@RequestParam int duration) {
        return courseService.getLongCourses(duration);
    }
}