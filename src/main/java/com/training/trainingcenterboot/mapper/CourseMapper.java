package com.training.trainingcenterboot.mapper;

import com.training.trainingcenterboot.dto.request.CourseRequest;
import com.training.trainingcenterboot.dto.response.CourseResponse;
import com.training.trainingcenterboot.model.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public Course toEntity(CourseRequest request) {
        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDuration(request.getDuration());
        course.setPrice(request.getPrice());
        return course;
    }

    public CourseResponse toResponse(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getDuration(),
                course.getPrice(),
                course.getTeacher().getId(),
                course.getTeacher().getName()
        );
    }
}