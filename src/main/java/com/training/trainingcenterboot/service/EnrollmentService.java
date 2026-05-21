package com.training.trainingcenterboot.service;

import com.training.trainingcenterboot.dto.request.EnrollmentRequest;
import com.training.trainingcenterboot.dto.request.ProgressRequest;
import com.training.trainingcenterboot.dto.response.EnrollmentResponse;
import com.training.trainingcenterboot.dto.response.PageResponse;
import com.training.trainingcenterboot.exception.BadRequestException;
import com.training.trainingcenterboot.exception.DuplicateResourceException;
import com.training.trainingcenterboot.exception.ResourceNotFoundException;
import com.training.trainingcenterboot.mapper.EnrollmentMapper;
import com.training.trainingcenterboot.model.Course;
import com.training.trainingcenterboot.model.Enrollment;
import com.training.trainingcenterboot.model.Student;
import com.training.trainingcenterboot.repository.EnrollmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentMapper enrollmentMapper;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             StudentService studentService,
                             CourseService courseService,
                             EnrollmentMapper enrollmentMapper) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentService = studentService;
        this.courseService = courseService;
        this.enrollmentMapper = enrollmentMapper;
    }

    public EnrollmentResponse enroll(EnrollmentRequest request) {
        if (enrollmentRepository.existsByStudentIdAndCourseId(
                request.getStudentId(),
                request.getCourseId()
        )) {
            throw new DuplicateResourceException("Студент уже записан на этот курс");
        }

        Student student = studentService.findStudentById(request.getStudentId());
        Course course = courseService.findCourseById(request.getCourseId());

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(LocalDate.now());
        enrollment.setProgress(0);
        enrollment.setPaymentStatus(false);

        return enrollmentMapper.toResponse(enrollmentRepository.save(enrollment));
    }

    public List<EnrollmentResponse> getAll() {
        return enrollmentRepository.findAll()
                .stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }

    public EnrollmentResponse pay(Long enrollmentId) {
        Enrollment enrollment = findEnrollmentById(enrollmentId);

        if (enrollment.isPaymentStatus()) {
            throw new BadRequestException("Курс уже оплачен");
        }

        enrollment.setPaymentStatus(true);

        return enrollmentMapper.toResponse(enrollmentRepository.save(enrollment));
    }

    public EnrollmentResponse updateProgress(Long enrollmentId, ProgressRequest request) {
        Enrollment enrollment = findEnrollmentById(enrollmentId);

        if (!enrollment.isPaymentStatus()) {
            throw new BadRequestException("Нельзя обновить прогресс без оплаты курса");
        }

        enrollment.setProgress(request.getProgress());

        return enrollmentMapper.toResponse(enrollmentRepository.save(enrollment));
    }

    public List<EnrollmentResponse> getPaidStudents() {
        return enrollmentRepository.findByPaymentStatus(true)
                .stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }

    public List<EnrollmentResponse> getSuccessfulStudents(int progress) {
        return enrollmentRepository.successfulStudents(progress)
                .stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }

    public List<EnrollmentResponse> getByCourseTitle(String title) {
        return enrollmentRepository.byCourseName(title)
                .stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }

    private Enrollment findEnrollmentById(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Запись на курс с id " + id + " не найдена")
                );
    }

    //Ниже по пагинации методы

    public PageResponse<EnrollmentResponse> getAll(Pageable pageable) {
        Page<EnrollmentResponse> page = enrollmentRepository.findAll(pageable)
                .map(enrollmentMapper::toResponse);

        return toPageResponse(page);
    }

    public PageResponse<EnrollmentResponse> filterByPaymentStatus(boolean paymentStatus,
                                                                  Pageable pageable) {
        Page<EnrollmentResponse> page = enrollmentRepository
                .findByPaymentStatus(paymentStatus, pageable)
                .map(enrollmentMapper::toResponse);

        return toPageResponse(page);
    }

    public PageResponse<EnrollmentResponse> filterByProgress(int progress,
                                                             Pageable pageable) {
        Page<EnrollmentResponse> page = enrollmentRepository
                .findByProgressGreaterThanEqual(progress, pageable)
                .map(enrollmentMapper::toResponse);

        return toPageResponse(page);
    }

    public PageResponse<EnrollmentResponse> searchByCourseTitle(String title,
                                                                Pageable pageable) {
        Page<EnrollmentResponse> page = enrollmentRepository
                .findByCourseTitleContainingIgnoreCase(title, pageable)
                .map(enrollmentMapper::toResponse);

        return toPageResponse(page);
    }

    private PageResponse<EnrollmentResponse> toPageResponse(Page<EnrollmentResponse> page) {
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