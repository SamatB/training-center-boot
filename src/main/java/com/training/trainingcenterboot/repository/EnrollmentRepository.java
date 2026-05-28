package com.training.trainingcenterboot.repository;

import com.training.trainingcenterboot.model.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByPaymentStatus(boolean paymentStatus);

    List<Enrollment> findByCourseId(Long courseId);

    @Query("SELECT e FROM Enrollment e WHERE e.progress >= :progress")
    List<Enrollment> successfulStudents(int progress);

    @Query("SELECT e FROM Enrollment e WHERE e.paymentStatus = false")
    List<Enrollment> unpaidStudents();

    @Query("""
            SELECT e
            FROM Enrollment e
            WHERE e.course.title = :courseTitle
            """)
    List<Enrollment> byCourseName(String courseTitle);

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    Enrollment findEnrollmentByStudent_Id(Long studentId);

    Page<Enrollment> findByPaymentStatus(boolean paymentStatus, Pageable pageable);

    Page<Enrollment> findByProgressGreaterThanEqual(int progress, Pageable pageable);

    Page<Enrollment> findByCourseTitleContainingIgnoreCase(String title, Pageable pageable);
}
