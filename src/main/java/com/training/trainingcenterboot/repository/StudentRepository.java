package com.training.trainingcenterboot.repository;

import com.training.trainingcenterboot.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByNameContainingIgnoreCase(String name);

    List<Student> findByAgeGreaterThanEqual(int age);

    List<Student> findByCourseId(Long courseId);

    boolean existsByNameAndCourseId(String name, Long courseId);

    @Query("""
       SELECT s
       FROM Student s
       WHERE s.age >= :age
       AND s.course.title = :courseTitle
       """)
    List<Student> findAdultStudentsByCourse(int age, String courseTitle);
}
