package com.training.trainingcenterboot.repository;

import com.training.trainingcenterboot.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByTitle(String title);

    boolean existsByTitle(String title);

    List<Course> findByTitleContainingIgnoreCase(String keyword);
}
