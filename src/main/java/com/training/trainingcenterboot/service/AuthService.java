package com.training.trainingcenterboot.service;

import com.training.trainingcenterboot.dto.request.StudentRequest;
import com.training.trainingcenterboot.dto.request.TeacherRequest;
import com.training.trainingcenterboot.exception.DuplicateResourceException;
import com.training.trainingcenterboot.model.AppUser;
import com.training.trainingcenterboot.model.Role;
import com.training.trainingcenterboot.model.Student;
import com.training.trainingcenterboot.model.Teacher;
import com.training.trainingcenterboot.repository.AppUserRepository;
import com.training.trainingcenterboot.repository.StudentRepository;
import com.training.trainingcenterboot.repository.TeacherRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AppUserRepository appUserRepository,
                       StudentRepository studentRepository,
                       TeacherRepository teacherRepository,
                       PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerStudent(StudentRequest request) {

        if (appUserRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username уже занят");
        }

        AppUser user = new AppUser();

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.STUDENT);

        AppUser savedUser = appUserRepository.save(user);

        Student student = new Student();

        student.setName(request.getName());
        student.setAge(request.getAge());
        student.setEmail(request.getEmail());
        student.setUser(savedUser);

        studentRepository.save(student);

        return "Student успешно зарегистрирован";
    }

    public String registerTeacher(TeacherRequest request) {

        if (appUserRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username уже занят");
        }

        AppUser user = new AppUser();

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.TEACHER);

        AppUser savedUser = appUserRepository.save(user);

        Teacher teacher = new Teacher();

        teacher.setName(request.getName());
        teacher.setExperience(request.getExperience());
        teacher.setUser(savedUser);

        teacherRepository.save(teacher);

        return "Teacher успешно зарегистрирован";
    }
}