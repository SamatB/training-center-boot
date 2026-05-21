package com.training.trainingcenterboot.service;

import com.training.trainingcenterboot.dto.response.PageResponse;
import com.training.trainingcenterboot.dto.response.TeacherResponse;
import com.training.trainingcenterboot.exception.ResourceNotFoundException;
import com.training.trainingcenterboot.mapper.TeacherMapper;
import com.training.trainingcenterboot.model.Teacher;
import com.training.trainingcenterboot.repository.TeacherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

//    public TeacherResponse create(TeacherRegisterRequest request) {
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

    //Ниже по пагинации методы
    public PageResponse<TeacherResponse> getAll(Pageable pageable) {
        Page<TeacherResponse> page = teacherRepository.findAll(pageable)
                .map(teacherMapper::toResponse);

        return toPageResponse(page);
    }

    public PageResponse<TeacherResponse> searchByName(String name, Pageable pageable) {
        Page<TeacherResponse> page = teacherRepository
                .findByNameContainingIgnoreCase(name, pageable)
                .map(teacherMapper::toResponse);

        return toPageResponse(page);
    }

    public PageResponse<TeacherResponse> filterByExperience(int experience,
                                                            Pageable pageable) {
        Page<TeacherResponse> page = teacherRepository
                .findByExperienceGreaterThanEqual(experience, pageable)
                .map(teacherMapper::toResponse);

        return toPageResponse(page);
    }

    private PageResponse<TeacherResponse> toPageResponse(Page<TeacherResponse> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}