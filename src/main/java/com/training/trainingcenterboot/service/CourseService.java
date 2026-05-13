// service/CourseService.java

package com.training.trainingcenterboot.service;

import com.training.trainingcenterboot.model.Course;
import com.training.trainingcenterboot.model.Teacher;
import com.training.trainingcenterboot.repository.CourseRepository;
import com.training.trainingcenterboot.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;

    public CourseService(CourseRepository courseRepository,
                         TeacherRepository teacherRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
    }

    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    public Course create(Course course, Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow();
        course.setTeacher(teacher);
        return courseRepository.save(course);
    }

    public List<Course> getByPriceLessThan(double price) {
        return courseRepository.findByPriceLessThan(price);
    }

    public List<Course> getLongCourses(int duration) {
        return courseRepository.longCourses(duration);
    }

    public List<Course> getCoursesByPriceRange(double min, double max) {
        return courseRepository.coursesByPriceRange(min, max);
    }

    public List<Course> getCoursesOrderedByPrice() {
        return courseRepository.orderByPrice();
    }
}