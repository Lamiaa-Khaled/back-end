package com.ems.ems_app.services;

import com.ems.ems_app.dto.AdminDTO;
import com.ems.ems_app.entities.Admin;
import com.ems.ems_app.entities.Specializations;
import com.ems.ems_app.entities.User;
import com.ems.ems_app.exception.ResourceNotFoundException;
import com.ems.ems_app.repos.AdminRepository;
import com.ems.ems_app.repos.SpecializationsRepository;
import com.ems.ems_app.repos.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SpecializationsRepository specializationsRepository;

    public AdminDTO getAdminById(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + id));
        return convertToDto(admin);
    }
@Transactional
    public List<AdminDTO> getAllAdmins() {
        List<Admin> admins = adminRepository.findAll();
        return admins.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public AdminDTO createAdmin(Admin admin) {
        // Check if the user and specialization exist before creating the admin
        User user = userRepository.findById(admin.getUser().getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + admin.getUser().getUserId()));
        Specializations specialization = specializationsRepository.findById(admin.getSpecializations().getSpecializationId())
                .orElseThrow(() -> new ResourceNotFoundException("Specialization not found with id: " + admin.getSpecializations().getSpecializationId()));

        admin.setUser(user);
        admin.setSpecializations(specialization);

        Admin savedAdmin = adminRepository.save(admin);
        return convertToDto(savedAdmin);
    }

    public AdminDTO updateAdmin(Long id, Admin adminDetails) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + id));

        // Check if the user and specialization exist before updating the admin
        User user = userRepository.findById(adminDetails.getUser().getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + adminDetails.getUser().getUserId()));
        Specializations specialization = specializationsRepository.findById(adminDetails.getSpecializations().getSpecializationId())
                .orElseThrow(() -> new ResourceNotFoundException("Specialization not found with id: " + adminDetails.getSpecializations().getSpecializationId()));

        admin.setUser(user);
        admin.setSpecializations(specialization);

        Admin updatedAdmin = adminRepository.save(admin);
        return convertToDto(updatedAdmin);
    }

    public void deleteAdmin(Long id) {
        if (!adminRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Admin not found with id: " + id);
        }
        adminRepository.deleteById(id);
    }

    private AdminDTO convertToDto(Admin admin) {
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setAdminId(admin.getAdminId());
        adminDTO.setUserId(admin.getUser().getUserId());
        adminDTO.setSpecializationId(admin.getSpecializations().getSpecializationId());
        return adminDTO;
    }}