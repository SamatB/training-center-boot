// controller/StudentController.java

package com.training.trainingcenterboot.controller;

import com.training.trainingcenterboot.model.Student;
import com.training.trainingcenterboot.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getAll() {
        return studentService.getAll();
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        return studentService.create(student);
    }

    @GetMapping("/age-greater-than")
    public List<Student> getByAgeGreaterThan(@RequestParam int age) {
        return studentService.getByAgeGreaterThan(age);
    }

    @GetMapping("/email")
    public Student getByEmail(@RequestParam String email) {
        return studentService.getByEmail(email);
    }

    @GetMapping("/age-between")
    public List<Student> getStudentsBetweenAges(@RequestParam int min,
                                                @RequestParam int max) {
        return studentService.getStudentsBetweenAges(min, max);
    }

    @GetMapping("/ordered-by-age")
    public List<Student> getStudentsOrderedByAge() {
        return studentService.getStudentsOrderedByAge();
    }
}