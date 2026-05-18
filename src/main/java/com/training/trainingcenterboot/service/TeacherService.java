package com.training.trainingcenterboot.service;

import com.training.trainingcenterboot.dto.request.TeacherRequest;
import com.training.trainingcenterboot.dto.response.TeacherResponse;
import com.training.trainingcenterboot.exception.ResourceNotFoundException;
import com.training.trainingcenterboot.mapper.TeacherMapper;
import com.training.trainingcenterboot.model.Teacher;
import com.training.trainingcenterboot.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    public TeacherService(TeacherRepository teacherRepository,
                          TeacherMapper teacherMapper) {
        this.teacherRepository = teacherRepository;
        this.teacherMapper = teacherMapper;
    }

    public List<TeacherResponse> getAll() {
        return teacherRepository.findAll()
                .stream()
                .map(teacherMapper::toResponse)
                .toList();
    }

//    public TeacherResponse create(TeacherRequest request) {
//        Teacher teacher = teacherMapper.toEntity(request);
//        return teacherMapper.toResponse(teacherRepository.save(teacher));
//    }

    public List<TeacherResponse> getExperiencedTeachers(int experience) {
        return teacherRepository.experiencedTeachers(experience)
                .stream()
                .map(teacherMapper::toResponse)
                .toList();
    }

    public Teacher findTeacherById(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Преподаватель с id " + id + " не найден")
                );
    }
}