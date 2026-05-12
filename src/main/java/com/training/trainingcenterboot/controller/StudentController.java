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

    @PostMapping("/course/{courseId}")
    public Student create(@RequestBody Student student,
                          @PathVariable Long courseId) {
        return studentService.create(student, courseId);
    }

    @GetMapping
    public List<Student> getAll() {
        return studentService.getAll();
    }

    @GetMapping("/{id}")
    public Student getById(@PathVariable Long id) {
        return studentService.getById(id);
    }

    @GetMapping("/search")
    public List<Student> search(@RequestParam String name) {
        return studentService.searchByName(name);
    }

    @GetMapping("/adults")
    public List<Student> getAdults() {
        return studentService.getAdults();
    }

    @GetMapping("/course/{courseId}")
    public List<Student> getByCourse(@PathVariable Long courseId) {
        return studentService.getByCourse(courseId);
    }

    @GetMapping("/adults/course")
    public List<Student> getAdultsByCourse(@RequestParam String title) {
        return studentService.getAdultStudentsByCourse(title);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable Long id,
                          @RequestBody Student student) {
        return studentService.update(id, student);
    }

    @PatchMapping("/{studentId}/course/{courseId}")
    public Student changeCourse(@PathVariable Long studentId,
                                @PathVariable Long courseId) {
        return studentService.changeCourse(studentId, courseId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        studentService.delete(id);
    }
}