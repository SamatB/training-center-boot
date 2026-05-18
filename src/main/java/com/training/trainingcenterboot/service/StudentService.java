package com.training.trainingcenterboot.service;

import com.training.trainingcenterboot.dto.request.StudentRequest;
import com.training.trainingcenterboot.dto.response.StudentResponse;
import com.training.trainingcenterboot.exception.DuplicateResourceException;
import com.training.trainingcenterboot.exception.ResourceNotFoundException;
import com.training.trainingcenterboot.mapper.StudentMapper;
import com.training.trainingcenterboot.model.Enrollment;
import com.training.trainingcenterboot.model.Student;
import com.training.trainingcenterboot.repository.EnrollmentRepository;
import com.training.trainingcenterboot.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentMapper studentMapper;

    public StudentService(StudentRepository studentRepository, EnrollmentRepository enrollmentRepository,
                          StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.studentMapper = studentMapper;
    }

    public List<StudentResponse> getAll() {
        return studentRepository.findAll()
                .stream()
                .map(studentMapper::toResponse)
                .toList();
    }

    public StudentResponse getById(Long id) {
        Student student = findStudentById(id);
        return studentMapper.toResponse(student);
    }

//    public StudentResponse create(StudentRequest request) {
//        if (studentRepository.existsByEmail(request.getEmail())) {
//            throw new DuplicateResourceException("Студент с таким email уже существует");
//        }
//
//        Student student = studentMapper.toEntity(request);
//        Student savedStudent = studentRepository.save(student);
//
//        return studentMapper.toResponse(savedStudent);
//    }

    public StudentResponse update(Long id, StudentRequest request) {
        Student student = findStudentById(id);

        student.setName(request.getName());
        student.setAge(request.getAge());
        student.setEmail(request.getEmail());

        return studentMapper.toResponse(studentRepository.save(student));
    }

    public void delete(Long id) {
        Student student = findStudentById(id);
        Enrollment enrollment = enrollmentRepository.findEnrollmentByStudent_Id(id);
        if (Objects.equals(enrollment.getStudent().getId(), student.getId())) {
            enrollment.setStudent(null);
            enrollmentRepository.save(enrollment);
        }
        studentRepository.delete(student);
    }

    public List<StudentResponse> getStudentsOlderThan(int age) {
        return studentRepository.findByAgeGreaterThan(age)
                .stream()
                .map(studentMapper::toResponse)
                .toList();
    }

    public List<StudentResponse> getStudentsBetweenAges(int min, int max) {
        return studentRepository.findStudentsBetweenAges(min, max)
                .stream()
                .map(studentMapper::toResponse)
                .toList();
    }

    public Student findStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Студент с id " + id + " не найден")
                );
    }
}