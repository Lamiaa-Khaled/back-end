package com.ems.ems_app.services;

import com.ems.ems_app.dto.SpecializationsDTO;
import com.ems.ems_app.entities.Specializations;
import com.ems.ems_app.exception.ResourceNotFoundException;
import com.ems.ems_app.repos.SpecializationsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecializationsService {

    @Autowired
    private SpecializationsRepository specializationsRepository;

    public SpecializationsDTO getSpecializationById(Long id) {
        Specializations specializations = specializationsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialization not found with id: " + id));
        return convertToDto(specializations);
    }

    public List<SpecializationsDTO> getAllSpecializations() {
        List<Specializations> specializations = specializationsRepository.findAll();
        return specializations.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public SpecializationsDTO createSpecialization(Specializations specializations) {
        Specializations savedSpecialization = specializationsRepository.save(specializations);
        return convertToDto(savedSpecialization);
    }

    public SpecializationsDTO updateSpecialization(Long id, Specializations specializationDetails) {
        Specializations specializations = specializationsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialization not found with id: " + id));

        specializations.setSpecializationName(specializationDetails.getSpecializationName());

        Specializations updatedSpecialization = specializationsRepository.save(specializations);
        return convertToDto(updatedSpecialization);
    }

    public void deleteSpecialization(Long id) {
        if (!specializationsRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Specialization not found with id: " + id);
        }
        specializationsRepository.deleteById(id);
    }

    private SpecializationsDTO convertToDto(Specializations specialization) {
        SpecializationsDTO specializationsDTO = new SpecializationsDTO();
        specializationsDTO.setSpecializationId(specialization.getSpecializationId());
        specializationsDTO.setSpecializationName(specialization.getSpecializationName());
        return specializationsDTO;
    }}
