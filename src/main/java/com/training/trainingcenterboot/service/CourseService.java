package com.training.trainingcenterboot.service;

import com.training.trainingcenterboot.dto.request.CourseRequest;
import com.training.trainingcenterboot.dto.response.CourseResponse;
import com.training.trainingcenterboot.dto.response.PageResponse;
import com.training.trainingcenterboot.exception.ResourceNotFoundException;
import com.training.trainingcenterboot.mapper.CourseMapper;
import com.training.trainingcenterboot.model.Course;
import com.training.trainingcenterboot.model.Teacher;
import com.training.trainingcenterboot.repository.CourseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final TeacherService teacherService;
    private final CourseMapper courseMapper;

    public CourseService(CourseRepository courseRepository,
                         TeacherService teacherService,
                         CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.teacherService = teacherService;
        this.courseMapper = courseMapper;
    }

    public List<CourseResponse> getAll() {
        return courseRepository.findAll()
                .stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    public CourseResponse create(CourseRequest request) {
        Teacher teacher = teacherService.findTeacherById(request.getTeacherId());

        Course course = courseMapper.toEntity(request);
        course.setTeacher(teacher);

        return courseMapper.toResponse(courseRepository.save(course));
    }

    public CourseResponse getById(Long id) {
        Course course = findCourseById(id);
        return courseMapper.toResponse(course);
    }

    public List<CourseResponse> getCheapCourses(double price) {
        return courseRepository.findByPriceLessThan(price)
                .stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    public List<CourseResponse> getLongCourses(int duration) {
        return courseRepository.findLongCourses(duration)
                .stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    public Course findCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Курс с id " + id + " не найден")
                );
    }


    //Ниже по пагинации методы
    public PageResponse<CourseResponse> getAll(Pageable pageable) {
        Page<CourseResponse> page = courseRepository.findAll(pageable)
                .map(courseMapper::toResponse);

        return toPageResponse(page);
    }

    public PageResponse<CourseResponse> searchByTitle(String title, Pageable pageable) {
        Page<CourseResponse> page = courseRepository
                .findByTitleContainingIgnoreCase(title, pageable)
                .map(courseMapper::toResponse);

        return toPageResponse(page);
    }

    public PageResponse<CourseResponse> filterByPrice(double minPrice,
                                                      double maxPrice,
                                                      Pageable pageable) {
        Page<CourseResponse> page = courseRepository
                .findByPriceBetween(minPrice, maxPrice, pageable)
                .map(courseMapper::toResponse);

        return toPageResponse(page);
    }

    public PageResponse<CourseResponse> filterByDuration(int duration, Pageable pageable) {
        Page<CourseResponse> page = courseRepository
                .findByDurationGreaterThanEqual(duration, pageable)
                .map(courseMapper::toResponse);

        return toPageResponse(page);
    }

    public PageResponse<CourseResponse> searchByTitleOrTeacher(String keyword,
                                                               Pageable pageable) {
        Page<CourseResponse> page = courseRepository
                .searchByTitleOrTeacherName(keyword, pageable)
                .map(courseMapper::toResponse);

        return toPageResponse(page);
    }

    private PageResponse<CourseResponse> toPageResponse(Page<CourseResponse> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}