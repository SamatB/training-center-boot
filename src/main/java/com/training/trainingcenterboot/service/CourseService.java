package com.training.trainingcenterboot.service;

import com.training.trainingcenterboot.model.Course;
import com.training.trainingcenterboot.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course create(Course course) {
        if (courseRepository.existsByTitle(course.getTitle())) {
            throw new RuntimeException("Курс с таким названием уже существует");
        }

        return courseRepository.save(course);
    }

    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    public Course getById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Курс не найден"));
    }

    public Course getByTitle(String title) {
        return courseRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Курс не найден"));
    }

    public List<Course> searchByTitle(String keyword) {
        return courseRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public Course update(Long id, Course updatedCourse) {
        Course course = getById(id);

        course.setTitle(updatedCourse.getTitle());

        return courseRepository.save(course);
    }

    public void delete(Long id) {
        Course course = getById(id);
        courseRepository.delete(course);
    }
}