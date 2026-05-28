package com.training.trainingcenterboot.controller.mvc;

import com.training.trainingcenterboot.dto.request.TeacherRegisterRequest;
import com.training.trainingcenterboot.service.AuthService;
import com.training.trainingcenterboot.service.TeacherService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TeacherViewController {

    private final TeacherService teacherService;
    private final AuthService authService;

    public TeacherViewController(TeacherService teacherService,
                                 AuthService authService) {
        this.teacherService = teacherService;
        this.authService = authService;
    }

    @GetMapping("/mvc/teachers")
    public String teachersPage(Model model) {
        model.addAttribute("teachers", teacherService.getAll());
        return "teacher/teachers";
    }

    @GetMapping("/mvc/teachers/create")
    public String createTeacherPage(Model model) {
        model.addAttribute("teacher", new TeacherRegisterRequest());
        return "teacher/teacher-create";
    }

    @PostMapping("/mvc/teachers/create")
    public String createTeacher(@ModelAttribute("teacher") TeacherRegisterRequest request) {
        authService.registerTeacher(request);
        return "redirect:/mvc/teachers";
    }

    @GetMapping("/mvc/teachers/search")
    public String searchTeachers(@RequestParam String name,
                                 Model model) {

        model.addAttribute(
                "teachers",
                teacherService.searchByName(name, Pageable.unpaged()).getContent()
        );

        model.addAttribute("searchValue", name);

        return "teacher/teachers";
    }
}