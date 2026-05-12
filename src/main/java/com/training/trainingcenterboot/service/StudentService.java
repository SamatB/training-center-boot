package com.training.trainingcenterboot.service;

import com.training.trainingcenterboot.model.Course;
import com.training.trainingcenterboot.model.Student;
import com.training.trainingcenterboot.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseService courseService;

    public StudentService(StudentRepository studentRepository,
                          CourseService courseService) {
        this.studentRepository = studentRepository;
        this.courseService = courseService;
    }

    public Student create(Student student, Long courseId) {
        Course course = courseService.getById(courseId);

        if (studentRepository.existsByNameAndCourseId(student.getName(), courseId)) {
            throw new RuntimeException("Студент уже записан на этот курс");
        }

        student.setCourse(course);

        return studentRepository.save(student);
    }

    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    public Student getById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Студент не найден"));
    }

    public List<Student> searchByName(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Student> getAdults() {
        return studentRepository.findByAgeGreaterThanEqual(18);
    }

    public List<Student> getByCourse(Long courseId) {
        return studentRepository.findByCourseId(courseId);
    }

    public List<Student> getAdultStudentsByCourse(String courseTitle) {
        return studentRepository
                .findAdultStudentsByCourse(18, courseTitle);
    }

    public Student update(Long id, Student updatedStudent) {
        Student student = getById(id);

        student.setName(updatedStudent.getName());
        student.setAge(updatedStudent.getAge());

        return studentRepository.save(student);
    }

    public Student changeCourse(Long studentId, Long courseId) {
        Student student = getById(studentId);
        Course course = courseService.getById(courseId);

        student.setCourse(course);

        return studentRepository.save(student);
    }

    public void delete(Long id) {
        Student student = getById(id);
        studentRepository.delete(student);
    }
}