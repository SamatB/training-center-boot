package com.training.trainingcenterboot.mapper;

import com.training.trainingcenterboot.dto.request.TeacherRequest;
import com.training.trainingcenterboot.dto.response.TeacherResponse;
import com.training.trainingcenterboot.model.Teacher;
import org.springframework.stereotype.Component;

@Component
public class TeacherMapper {

    public Teacher toEntity(TeacherRequest request) {
        Teacher teacher = new Teacher();
        teacher.setName(request.getName());
        teacher.setExperience(request.getExperience());
        return teacher;
    }

    public TeacherResponse toResponse(Teacher teacher) {
        return new TeacherResponse(
                teacher.getId(),
                teacher.getName(),
                teacher.getExperience()
        );
    }
}