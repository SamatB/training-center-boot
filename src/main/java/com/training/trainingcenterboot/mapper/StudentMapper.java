package com.training.trainingcenterboot.mapper;

import com.training.trainingcenterboot.dto.request.StudentRegisterRequest;
import com.training.trainingcenterboot.dto.response.StudentResponse;
import com.training.trainingcenterboot.model.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public Student toEntity(StudentRegisterRequest request) {
        Student student = new Student();
        student.setName(request.getName());
        student.setAge(request.getAge());
        student.setEmail(request.getEmail());
        return student;
    }

    public StudentResponse toResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getName(),
                student.getAge(),
                student.getEmail()
        );
    }
}