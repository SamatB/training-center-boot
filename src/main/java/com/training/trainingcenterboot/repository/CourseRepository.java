package com.training.trainingcenterboot.repository;

import com.training.trainingcenterboot.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByPriceLessThan(double price);

    @Query("SELECT c FROM Course c WHERE c.duration > :duration")
    List<Course> findLongCourses(int duration);

    @Query("SELECT c FROM Course c WHERE c.price BETWEEN :min AND :max")
    List<Course> coursesByPriceRange(double min, double max);

    @Query("SELECT c FROM Course c ORDER BY c.price ASC")
    List<Course> orderByPrice();

    Page<Course> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Course> findByPriceBetween(double minPrice, double maxPrice, Pageable pageable);

    Page<Course> findByDurationGreaterThanEqual(int duration, Pageable pageable);

    @Query("""
            SELECT c FROM Course c
            WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(c.teacher.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<Course> searchByTitleOrTeacherName(String keyword, Pageable pageable);
}
