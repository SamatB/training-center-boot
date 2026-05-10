package com.training.trainingcenterboot.service;

import com.training.trainingcenterboot.model.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    private final List<Student> students = new ArrayList<>();
    private Long nextId = 1L;

    public List<Student> getAll() {
        return students;
    }

    public Student getById(Long id) {
        return students.stream()
                .filter(student -> student.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Student create(Student student) {
        student.setId(nextId++);
        students.add(student);
        return student;
    }

    public Student update(Long id, Student newStudent) {
        Student student = getById(id);

        if (student == null) {
            return null;
        }

        student.setName(newStudent.getName());
        student.setAge(newStudent.getAge());

        return student;
    }

    public boolean delete(Long id) {
        return students.removeIf(student -> student.getId().equals(id));
    }
}