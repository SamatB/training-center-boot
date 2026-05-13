package com.training.trainingcenterboot.repository;

import com.training.trainingcenterboot.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByPaymentStatus(boolean paymentStatus);

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
}
