package com.ems.ems_app.services;

import com.ems.ems_app.dto.responseDTO.ClassStudyResponseDTO;
import com.ems.ems_app.entities.ClassStudy;
import com.ems.ems_app.exception.ClassStudyNotFoundException;
import com.ems.ems_app.repos.ClassStudyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClassStudyService {

    private final ClassStudyRepository classStudyRepository;

    @Autowired
    public ClassStudyService(ClassStudyRepository classStudyRepository) {
        this.classStudyRepository = classStudyRepository;
    }

    public ClassStudyResponseDTO createClassStudy(int year, BigDecimal totalGrade) {
        ClassStudy classStudy = new ClassStudy();
        classStudy.setYear(year);
        classStudy.setTotalGrade(totalGrade);
        ClassStudy savedClassStudy = classStudyRepository.save(classStudy);
        return ClassStudyResponseDTO.convertToClassStudyResponseDTO(savedClassStudy);
    }

    public ClassStudyResponseDTO getClassStudyById(UUID classId) {
        ClassStudy classStudy = classStudyRepository.findById(classId)
                .orElseThrow(() -> new ClassStudyNotFoundException("ClassStudy not found with id: " + classId));
        return ClassStudyResponseDTO.convertToClassStudyResponseDTO(classStudy);
    }

    public List<ClassStudyResponseDTO> getAllClassStudies() {
        List<ClassStudy> classStudies = classStudyRepository.findAll();
        return classStudies.stream()
                .map(ClassStudyResponseDTO::convertToClassStudyResponseDTO)
                .collect(Collectors.toList());
    }

    public ClassStudyResponseDTO updateClassStudy(UUID classId, int year, BigDecimal totalGrade) {
        ClassStudy existingClassStudy = classStudyRepository.findById(classId)
                .orElseThrow(() -> new ClassStudyNotFoundException("ClassStudy not found with id: " + classId));

        existingClassStudy.setYear(year);
        existingClassStudy.setTotalGrade(totalGrade);

        ClassStudy updatedClassStudy = classStudyRepository.update(existingClassStudy);
        return ClassStudyResponseDTO.convertToClassStudyResponseDTO(updatedClassStudy);
    }

    public void deleteClassStudy(UUID classId) {
        classStudyRepository.deleteById(classId);
    }
}
