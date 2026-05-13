// service/StudentService.java

package com.training.trainingcenterboot.service;

import com.training.trainingcenterboot.model.Student;
import com.training.trainingcenterboot.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    public Student create(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> getByAgeGreaterThan(int age) {
        return studentRepository.findByAgeGreaterThan(age);
    }

    public Student getByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    public List<Student> getStudentsBetweenAges(int min, int max) {
        return studentRepository.findStudentsBetweenAges(min, max);
    }

    public List<Student> getStudentsOrderedByAge() {
        return studentRepository.findStudentsOrderedByAge();
    }
}