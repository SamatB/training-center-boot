package com.training.trainingcenterboot.repository;

import com.training.trainingcenterboot.model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    @Query("SELECT t FROM Teacher t WHERE t.experience >= :experience")
    List<Teacher> experiencedTeachers(int experience);

    @Query("SELECT t FROM Teacher t ORDER BY t.name ASC")
    List<Teacher> sortedTeachers();

    Page<Teacher> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Teacher> findByExperienceGreaterThanEqual(int experience, Pageable pageable);
}
