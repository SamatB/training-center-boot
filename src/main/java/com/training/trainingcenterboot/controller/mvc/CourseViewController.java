package com.training.trainingcenterboot.controller.mvc;

import com.training.trainingcenterboot.dto.request.CourseRequest;
import com.training.trainingcenterboot.service.CourseService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CourseViewController {

    private final CourseService courseService;

    public CourseViewController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/mvc/courses")
    public String coursesPage(Model model) {
        model.addAttribute("courses", courseService.getAll());
        return "course/courses";
    }

    @GetMapping("/mvc/courses/create")
    public String createCoursePage(Model model) {
        model.addAttribute("course", new CourseRequest());
        return "course/course-create";
    }

    @PostMapping("/mvc/courses/create")
    public String createCourse(@ModelAttribute("course") CourseRequest request) {
        courseService.create(request);
        return "redirect:/mvc/courses";
    }

    @GetMapping("/mvc/courses/edit/{id}")
    public String editCoursePage(@PathVariable Long id, Model model) {
        model.addAttribute("courseId", id);
        model.addAttribute("course", courseService.getById(id));
        return "course/course-edit";
    }

    @PostMapping("/mvc/courses/edit/{id}")
    public String updateCourse(@PathVariable Long id,
                               @ModelAttribute("course") CourseRequest request) {
        courseService.update(id, request);
        return "redirect:/mvc/courses";
    }

    @PostMapping("/mvc/courses/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.delete(id);
        return "redirect:/mvc/courses";
    }

    @GetMapping("/mvc/courses/search")
    public String searchCourses(@RequestParam String title, Model model) {

        model.addAttribute(
                "courses",
                courseService.searchByTitle(title, Pageable.unpaged()).getContent()
        );

        model.addAttribute("searchValue", title);

        return "course/courses";
    }
}