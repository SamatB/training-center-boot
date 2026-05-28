package com.training.trainingcenterboot.controller.rest;

import com.training.trainingcenterboot.dto.response.AdminResponse;
import com.training.trainingcenterboot.service.AdminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public List<AdminResponse> getAll() {
        return adminService.getAll();
    }

    @GetMapping("/{id}")
    public AdminResponse getById(@PathVariable Long id) {
        return adminService.getById(id);
    }
}