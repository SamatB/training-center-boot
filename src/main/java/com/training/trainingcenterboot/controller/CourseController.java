package com.training.trainingcenterboot.controller;

import com.training.trainingcenterboot.model.Course;
import com.training.trainingcenterboot.service.CourseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public Course create(@RequestBody Course course) {
        return courseService.create(course);
    }

    @GetMapping
    public List<Course> getAll() {
        return courseService.getAll();
    }

    @GetMapping("/{id}")
    public Course getById(@PathVariable Long id) {
        return courseService.getById(id);
    }

    @GetMapping("/title/{title}")
    public Course getByTitle(@PathVariable String title) {
        return courseService.getByTitle(title);
    }

    @GetMapping("/search")
    public List<Course> search(@RequestParam String keyword) {
        return courseService.searchByTitle(keyword);
    }

    @PutMapping("/{id}")
    public Course update(@PathVariable Long id,
                         @RequestBody Course course) {
        return courseService.update(id, course);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        courseService.delete(id);
    }
}