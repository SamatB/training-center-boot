package com.training.trainingcenterboot.mapper;

import com.training.trainingcenterboot.dto.request.AdminRegisterRequest;
import com.training.trainingcenterboot.dto.response.AdminResponse;
import com.training.trainingcenterboot.model.Admin;
import org.springframework.stereotype.Component;

@Component
public class AdminMapper {

    public Admin toEntity(AdminRegisterRequest request) {
        Admin admin = new Admin();
        admin.setName(request.getName());
        admin.setEmail(request.getEmail());
        return admin;
    }

    public AdminResponse toResponse(Admin admin) {
        return new AdminResponse(
                admin.getId(),
                admin.getName(),
                admin.getEmail()
        );
    }
}