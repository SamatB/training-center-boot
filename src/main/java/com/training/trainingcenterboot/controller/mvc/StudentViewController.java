package com.training.trainingcenterboot.controller.mvc;

import com.training.trainingcenterboot.dto.request.StudentRegisterRequest;
import com.training.trainingcenterboot.dto.response.PageResponse;
import com.training.trainingcenterboot.dto.response.StudentResponse;
import com.training.trainingcenterboot.service.AuthService;
import com.training.trainingcenterboot.service.StudentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class StudentViewController {

    private final StudentService studentService;
    private final AuthService authService;

    public StudentViewController(StudentService studentService,
                                 AuthService authService) {
        this.studentService = studentService;
        this.authService = authService;
    }

    @GetMapping("/mvc/students")
    public String studentsPage(Model model) {
        model.addAttribute("students", studentService.getAll()); //model.addAttribute:  Java → HTML
        model.addAttribute("currentPage", null);
        model.addAttribute("totalPages", null);
        return "student/students";
    }

    @GetMapping("/mvc/students/create")
    public String createStudentPage(Model model) {
        model.addAttribute("student", new StudentRegisterRequest());
        return "student/student-create";
    }

    @PostMapping("/mvc/students/create")
    public String createStudent(@ModelAttribute("student") StudentRegisterRequest request) { //@ModelAttribute: HTML → Java
        authService.registerStudent(request);
        return "redirect:/mvc/students";
    }

    @GetMapping("/mvc/students/edit/{id}")
    public String editStudentPage(@PathVariable Long id, Model model) {
        model.addAttribute("studentId", id);
        model.addAttribute("student", studentService.getById(id));
        return "student/student-edit";
    }

    @PostMapping("/mvc/students/edit/{id}")
    public String updateStudent(@PathVariable Long id,
                                @ModelAttribute("student") StudentRegisterRequest request) {
        studentService.update(id, request);
        return "redirect:/mvc/students";
    }

    @PostMapping("/mvc/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.delete(id);
        return "redirect:/mvc/students";
    }

    @GetMapping("/mvc/students/search")
    public String searchStudents(@RequestParam String name, Model model) {
        model.addAttribute("students", studentService.searchByName(name, Pageable.unpaged()).getContent());
        model.addAttribute("searchValue", name);
        model.addAttribute("currentPage", null);
        model.addAttribute("totalPages", null);
        return "student/students";
    }

    @GetMapping("/mvc/students/page")
    public String studentsPageable(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "5") int size,
                                   Model model) {

        PageResponse<StudentResponse> response =
                studentService.getAll(PageRequest.of(page, size));

        model.addAttribute("students", response.getContent());

        model.addAttribute("currentPage", page);

        model.addAttribute("totalPages", response.getTotalPages());

        return "student/students";
    }
}