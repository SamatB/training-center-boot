package com.training.trainingcenterboot.service;

import com.training.trainingcenterboot.dto.request.*;
import com.training.trainingcenterboot.dto.response.AuthResponse;
import com.training.trainingcenterboot.exception.DuplicateResourceException;
import com.training.trainingcenterboot.model.*;
import com.training.trainingcenterboot.repository.*;
import com.training.trainingcenterboot.security.JwtService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(AppUserRepository appUserRepository,
                       StudentRepository studentRepository,
                       TeacherRepository teacherRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.appUserRepository = appUserRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public String registerStudent(StudentRegisterRequest request) {
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

    public String registerTeacher(TeacherRegisterRequest request) {
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

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token);
    }
}