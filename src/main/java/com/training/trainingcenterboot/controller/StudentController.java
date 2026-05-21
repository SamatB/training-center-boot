package com.training.trainingcenterboot.controller;

import com.training.trainingcenterboot.dto.request.StudentRegisterRequest;
import com.training.trainingcenterboot.dto.response.PageResponse;
import com.training.trainingcenterboot.dto.response.StudentResponse;
import com.training.trainingcenterboot.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
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
    public PageResponse<StudentResponse> getAll(Pageable pageable) {
        return studentService.getAll(pageable);
    }

    @GetMapping("/search")
    public PageResponse<StudentResponse> searchByName(@RequestParam String name,
                                                      Pageable pageable) {
        return studentService.searchByName(name, pageable);
    }

    @GetMapping("/filter/age")
    public PageResponse<StudentResponse> filterByAge(@RequestParam int minAge,
                                                     @RequestParam int maxAge,
                                                     Pageable pageable) {
        return studentService.filterByAge(minAge, maxAge, pageable);
    }

    @GetMapping("/search/full")
    public PageResponse<StudentResponse> searchByNameOrEmail(@RequestParam String keyword,
                                                             Pageable pageable) {
        return studentService.searchByNameOrEmail(keyword, pageable);
    }

//    @GetMapping
//    public List<StudentResponse> getAll() {
//        return studentService.getAll();
//    }

    @GetMapping("/{id}")
    public StudentResponse getById(@PathVariable Long id) {
        return studentService.getById(id);
    }

//    @PostMapping
//    public StudentResponse create(@Valid @RequestBody StudentRegisterRequest request) {
//        return studentService.create(request);
//    }

    @PutMapping("/{id}")
    public StudentResponse update(@PathVariable Long id,
                                  @Valid @RequestBody StudentRegisterRequest request) {
        return studentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        studentService.delete(id);
        return "Студент успешно удален";
    }

    @GetMapping("/older-than")
    public List<StudentResponse> olderThan(@RequestParam int age) {
        return studentService.getStudentsOlderThan(age);
    }

    @GetMapping("/between-ages")
    public List<StudentResponse> betweenAges(@RequestParam int min,
                                             @RequestParam int max) {
        return studentService.getStudentsBetweenAges(min, max);
    }
}