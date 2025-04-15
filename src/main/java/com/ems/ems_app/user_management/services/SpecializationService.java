package com.ems.ems_app.user_management.services;

import com.ems.ems_app.user_management.dto.responseDTO.SpecializationResponseDTO;
import com.ems.ems_app.user_management.entities.Specialization;
import com.ems.ems_app.user_management.exception.SpecializationNotFoundException;
import com.ems.ems_app.user_management.repos.SpecializationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SpecializationService {

    private final SpecializationRepository specializationRepository;
    private static final Logger logger = LoggerFactory.getLogger(SpecializationService.class);

    @Autowired
    public SpecializationService(SpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }

    public SpecializationResponseDTO createSpecialization(String specializationName) {
        System.out.println("Specialization name to be saved " + specializationName);
        Specialization specialization = new Specialization();
        specialization.setSpecializationName(specializationName);
        Specialization savedSpecialization = specializationRepository.save(specialization);
        return SpecializationResponseDTO.convertToSpecializationResponseDTO(savedSpecialization);
    }

    public SpecializationResponseDTO getSpecializationById(UUID specializationId) {

        Specialization specialization = specializationRepository.findById(specializationId)
                .orElseThrow(() -> new SpecializationNotFoundException("Specialization not found with id: " + specializationId));

        logger.info("Specialization retrieved from database: {}", specialization);
        SpecializationResponseDTO responseDTO = SpecializationResponseDTO.convertToSpecializationResponseDTO(specialization);

        logger.info("SpecializationResponseDTO after conversion: {}", responseDTO);

        return responseDTO;    }

    public List<SpecializationResponseDTO> getAllSpecializations() {
        List<Specialization> specializations = specializationRepository.findAll();
        return specializations.stream()
                .map(SpecializationResponseDTO::convertToSpecializationResponseDTO)
                .collect(Collectors.toList());
    }

    public SpecializationResponseDTO updateSpecialization(UUID specializationId, String specializationName) {
        Specialization existingSpecialization = specializationRepository.findById(specializationId)
                .orElseThrow(() -> new SpecializationNotFoundException("Specialization not found with id: " + specializationId));

        existingSpecialization.setSpecializationName(specializationName);
        Specialization updatedSpecialization = specializationRepository.update(existingSpecialization);
        return SpecializationResponseDTO.convertToSpecializationResponseDTO(updatedSpecialization);
    }

    public void deleteSpecialization(UUID specializationId) {
        specializationRepository.deleteById(specializationId);
    }
}