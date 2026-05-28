package com.training.trainingcenterboot.service;

import com.training.trainingcenterboot.dto.response.AdminResponse;
import com.training.trainingcenterboot.exception.ResourceNotFoundException;
import com.training.trainingcenterboot.mapper.AdminMapper;
import com.training.trainingcenterboot.model.Admin;
import com.training.trainingcenterboot.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;

    public AdminService(AdminRepository adminRepository,
                        AdminMapper adminMapper) {
        this.adminRepository = adminRepository;
        this.adminMapper = adminMapper;
    }

    public List<AdminResponse> getAll() {
        return adminRepository.findAll()
                .stream()
                .map(adminMapper::toResponse)
                .toList();
    }

    public AdminResponse getById(Long id) {
        Admin admin = findAdminById(id);
        return adminMapper.toResponse(admin);
    }

    public Admin findAdminById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Админ с id " + id + " не найден")
                );
    }

    public Admin findByUsername(String username) {
        return adminRepository.findByUserUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Админ с username " + username + " не найден")
                );
    }
}