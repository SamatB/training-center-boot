// controller/CourseController.java

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

    @GetMapping
    public List<Course> getAll() {
        return courseService.getAll();
    }

    @PostMapping("/{teacherId}")
    public Course create(@RequestBody Course course,
                         @PathVariable Long teacherId) {
        return courseService.create(course, teacherId);
    }

    @GetMapping("/price-less-than")
    public List<Course> getByPriceLessThan(@RequestParam double price) {
        return courseService.getByPriceLessThan(price);
    }

    @GetMapping("/long")
    public List<Course> getLongCourses(@RequestParam int duration) {
        return courseService.getLongCourses(duration);
    }

    @GetMapping("/price-range")
    public List<Course> getCoursesByPriceRange(@RequestParam double min,
                                               @RequestParam double max) {
        return courseService.getCoursesByPriceRange(min, max);
    }

    @GetMapping("/ordered-by-price")
    public List<Course> getCoursesOrderedByPrice() {
        return courseService.getCoursesOrderedByPrice();
    }
}