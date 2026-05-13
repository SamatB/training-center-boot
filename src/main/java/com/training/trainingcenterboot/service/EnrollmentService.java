// service/EnrollmentService.java

package com.training.trainingcenterboot.service;

import com.training.trainingcenterboot.model.Course;
import com.training.trainingcenterboot.model.Enrollment;
import com.training.trainingcenterboot.model.Student;
import com.training.trainingcenterboot.repository.CourseRepository;
import com.training.trainingcenterboot.repository.EnrollmentRepository;
import com.training.trainingcenterboot.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             StudentRepository studentRepository,
                             CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public List<Enrollment> getAll() {
        return enrollmentRepository.findAll();
    }

    public Enrollment enroll(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(LocalDate.now());
        enrollment.setProgress(0);
        enrollment.setPaymentStatus(false);

        return enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> getByPaymentStatus(boolean paymentStatus) {
        return enrollmentRepository.findByPaymentStatus(paymentStatus);
    }

    public List<Enrollment> getSuccessfulStudents(int progress) {
        return enrollmentRepository.successfulStudents(progress);
    }

    public List<Enrollment> getUnpaidStudents() {
        return enrollmentRepository.unpaidStudents();
    }

    public List<Enrollment> getByCourseName(String courseTitle) {
        return enrollmentRepository.byCourseName(courseTitle);
    }
}