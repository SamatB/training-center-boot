// service/TeacherService.java

package com.training.trainingcenterboot.service;

import com.training.trainingcenterboot.model.Teacher;
import com.training.trainingcenterboot.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public List<Teacher> getAll() {
        return teacherRepository.findAll();
    }

    public Teacher create(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public Teacher update(Long id, Teacher newTeacher) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow();
        teacher.setName(newTeacher.getName());
        teacher.setExperience(newTeacher.getExperience());
        teacherRepository.save(teacher);
        return teacher;
    }

    public List<Teacher> getExperiencedTeachers(int experience) {
        return teacherRepository.experiencedTeachers(experience);
    }

    public List<Teacher> getSortedTeachers() {
        return teacherRepository.sortedTeachers();
    }
}