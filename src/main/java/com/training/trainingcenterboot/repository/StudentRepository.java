package com.training.trainingcenterboot.repository;

import com.training.trainingcenterboot.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByAgeGreaterThan(int age);

    Student findByEmail(String email);

    @Query("SELECT s FROM Student s WHERE s.age BETWEEN :min AND :max")
    List<Student> findStudentsBetweenAges(int min, int max);

    @Query("SELECT s FROM Student s ORDER BY s.age DESC")
    List<Student> findStudentsOrderedByAge();
}
