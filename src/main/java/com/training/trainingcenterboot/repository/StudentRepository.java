package com.training.trainingcenterboot.repository;

import com.training.trainingcenterboot.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByAgeGreaterThan(int age);

    Student findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT s FROM Student s WHERE s.age BETWEEN :min AND :max")
    List<Student> findStudentsBetweenAges(int min, int max);

    @Query("SELECT s FROM Student s ORDER BY s.age DESC")
    List<Student> findStudentsOrderedByAge();

    Page<Student> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Student> findByAgeBetween(int minAge, int maxAge, Pageable pageable);

    @Query("""
            SELECT s FROM Student s
            WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<Student> searchByNameOrEmail(String keyword, Pageable pageable);
}
