package com.ems.ems_app.user_management.services;

import com.ems.ems_app.user_management.dto.requestDTO.AdminRequestDTO;
import com.ems.ems_app.user_management.dto.responseDTO.AdminResponseDTO;
import com.ems.ems_app.user_management.entities.Admin;
import com.ems.ems_app.user_management.entities.Specialization;
import com.ems.ems_app.user_management.entities.User;
import com.ems.ems_app.user_management.exception.AdminNotFoundException;
import com.ems.ems_app.user_management.exception.SpecializationNotFoundException;
import com.ems.ems_app.user_management.exception.UserNotFoundException;
import com.ems.ems_app.user_management.repos.AdminRepository;
import com.ems.ems_app.user_management.repos.SpecializationRepository;
import com.ems.ems_app.user_management.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final SpecializationRepository specializationRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository, UserRepository userRepository, SpecializationRepository specializationRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.specializationRepository = specializationRepository;
    }

    public AdminResponseDTO createAdmin(AdminRequestDTO adminRequestDTO) {
        User user = userRepository.findById(adminRequestDTO.getUserRequestDTO().getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + adminRequestDTO.getUserRequestDTO().getUserId()));

        Specialization specialization = specializationRepository.findById(adminRequestDTO.getSpecializationId())
                .orElseThrow(() -> new SpecializationNotFoundException("Specialization not found with id: " + adminRequestDTO.getSpecializationId()));

        Admin admin = new Admin();
        admin.setUser(user);
        admin.setSpecialization(specialization);
        Admin savedAdmin = adminRepository.save(admin);
        return AdminResponseDTO.convertToAdminResponseDTO(savedAdmin);
    }

    public AdminResponseDTO getAdminById(UUID adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found with id: " + adminId));
        return AdminResponseDTO.convertToAdminResponseDTO(admin);
    }

    public List<AdminResponseDTO> getAllAdmins() {
        List<Admin> admins = adminRepository.findAll();
        return admins.stream()
                .map(AdminResponseDTO::convertToAdminResponseDTO)
                .collect(Collectors.toList());
    }

    public AdminResponseDTO updateAdmin(UUID adminId, AdminRequestDTO adminRequestDTO) {
        Admin existingAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found with id: " + adminId));

        User user = userRepository.findById(adminRequestDTO.getUserRequestDTO().getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + adminRequestDTO.getUserRequestDTO().getUserId()));

        Specialization specialization = specializationRepository.findById(adminRequestDTO.getSpecializationId())
                .orElseThrow(() -> new SpecializationNotFoundException("Specialization not found with id: " + adminRequestDTO.getSpecializationId()));

        existingAdmin.setUser(user);
        existingAdmin.setSpecialization(specialization);

        Admin updatedAdmin = adminRepository.update(existingAdmin);
        return AdminResponseDTO.convertToAdminResponseDTO(updatedAdmin);
    }

    public void deleteAdmin(UUID adminId) {
        adminRepository.deleteById(adminId);
    }
}
