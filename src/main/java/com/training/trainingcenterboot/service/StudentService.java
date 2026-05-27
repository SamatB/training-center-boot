package com.training.trainingcenterboot.service;

import com.training.trainingcenterboot.dto.request.StudentRegisterRequest;
import com.training.trainingcenterboot.dto.response.PageResponse;
import com.training.trainingcenterboot.dto.response.StudentResponse;
import com.training.trainingcenterboot.exception.ResourceNotFoundException;
import com.training.trainingcenterboot.mapper.StudentMapper;
import com.training.trainingcenterboot.model.Enrollment;
import com.training.trainingcenterboot.model.Student;
import com.training.trainingcenterboot.repository.EnrollmentRepository;
import com.training.trainingcenterboot.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

//    public StudentResponse create(StudentRegisterRequest request) {
//        if (studentRepository.existsByEmail(request.getEmail())) {
//            throw new DuplicateResourceException("Студент с таким email уже существует");
//        }
//
//        Student student = studentMapper.toEntity(request);
//        Student savedStudent = studentRepository.save(student);
//
//        return studentMapper.toResponse(savedStudent);
//    }

    public StudentResponse update(Long id, StudentRegisterRequest request) {
        Student student = findStudentById(id);

        student.setName(request.getName());
        student.setAge(request.getAge());
        student.setEmail(request.getEmail());

        return studentMapper.toResponse(studentRepository.save(student));
    }

    public void delete(Long id) {
        Student student = findStudentById(id);
        Enrollment enrollment = enrollmentRepository.findEnrollmentByStudent_Id(id);
        if (enrollment != null && enrollment.getStudent() != null) {
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


    //Ниже по пагинации методы

    public PageResponse<StudentResponse> getAll(Pageable pageable) {
        Page<StudentResponse> page = studentRepository.findAll(pageable)
                .map(studentMapper::toResponse);

        return toPageResponse(page);
    }

    public PageResponse<StudentResponse> searchByName(String name, Pageable pageable) {
        Page<StudentResponse> page = studentRepository
                .findByNameContainingIgnoreCase(name, pageable)
                .map(studentMapper::toResponse);

        return toPageResponse(page);
    }

    public PageResponse<StudentResponse> filterByAge(int minAge, int maxAge, Pageable pageable) {
        Page<StudentResponse> page = studentRepository
                .findByAgeBetween(minAge, maxAge, pageable)
                .map(studentMapper::toResponse);

        return toPageResponse(page);
    }

    public PageResponse<StudentResponse> searchByNameOrEmail(String keyword, Pageable pageable) {
        Page<StudentResponse> page = studentRepository
                .searchByNameOrEmail(keyword, pageable)
                .map(studentMapper::toResponse);

        return toPageResponse(page);
    }

    private PageResponse<StudentResponse> toPageResponse(Page<StudentResponse> page) {
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