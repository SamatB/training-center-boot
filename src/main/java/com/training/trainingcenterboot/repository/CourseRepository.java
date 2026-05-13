package com.training.trainingcenterboot.repository;

import com.training.trainingcenterboot.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByPriceLessThan(double price);

    @Query("SELECT c FROM Course c WHERE c.duration > :duration")
    List<Course> longCourses(int duration);

    @Query("SELECT c FROM Course c WHERE c.price BETWEEN :min AND :max")
    List<Course> coursesByPriceRange(double min, double max);

    @Query("SELECT c FROM Course c ORDER BY c.price ASC")
    List<Course> orderByPrice();
}
