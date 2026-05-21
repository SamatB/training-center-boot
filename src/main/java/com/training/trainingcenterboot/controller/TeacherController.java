package com.training.trainingcenterboot.controller;

import com.training.trainingcenterboot.dto.response.PageResponse;
import com.training.trainingcenterboot.dto.response.TeacherResponse;
import com.training.trainingcenterboot.service.TeacherService;
import org.springframework.data.domain.Pageable;
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
    public PageResponse<TeacherResponse> getAll(Pageable pageable) {
        return teacherService.getAll(pageable);
    }

    @GetMapping("/search")
    public PageResponse<TeacherResponse> searchByName(@RequestParam String name,
                                                      Pageable pageable) {
        return teacherService.searchByName(name, pageable);
    }

    @GetMapping("/filter/experience")
    public PageResponse<TeacherResponse> filterByExperience(@RequestParam int experience,
                                                            Pageable pageable) {
        return teacherService.filterByExperience(experience, pageable);
    }

//    @GetMapping
//    public List<TeacherResponse> getAll() {
//        return teacherService.getAll();
//    }

//    @PostMapping
//    public TeacherResponse create(@Valid @RequestBody TeacherRegisterRequest request) {
//        return teacherService.create(request);
//    }

    @GetMapping("/experienced")
    public List<TeacherResponse> experienced(@RequestParam int experience) {
        return teacherService.getExperiencedTeachers(experience);
    }
}