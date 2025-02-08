package com.ems.ems_app.services;


import com.ems.ems_app.dto.ClassStudyDTO;
import com.ems.ems_app.entities.ClassStudy;
import com.ems.ems_app.exception.ResourceNotFoundException;
import com.ems.ems_app.repos.ClassStudyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassStudyService {

    @Autowired
    private ClassStudyRepository classStudyRepository;

    public ClassStudyDTO getClassStudyById(Long id) {
        ClassStudy classStudy = classStudyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClassStudy not found with id: " + id));
        return convertToDto(classStudy);
    }

    public List<ClassStudyDTO> getAllClassStudies() {
        List<ClassStudy> classStudies = classStudyRepository.findAll();
        return classStudies.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ClassStudyDTO createClassStudy(ClassStudy classStudy) {
        ClassStudy savedClassStudy = classStudyRepository.save(classStudy);
        return convertToDto(savedClassStudy);
    }

    public ClassStudyDTO updateClassStudy(Long id, ClassStudy classStudyDetails) {
        ClassStudy classStudy = classStudyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClassStudy not found with id: " + id));

        classStudy.setYear(classStudyDetails.getYear());
        classStudy.setTotalGrade(classStudyDetails.getTotalGrade());

        ClassStudy updatedClassStudy = classStudyRepository.save(classStudy);
        return convertToDto(updatedClassStudy);
    }

    public void deleteClassStudy(Long id) {
        if (!classStudyRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("ClassStudy not found with id: " + id);
        }
        classStudyRepository.deleteById(id);
    }

    private ClassStudyDTO convertToDto(ClassStudy classStudy) {
        ClassStudyDTO classStudyDTO = new ClassStudyDTO();
        classStudyDTO.setClassId(classStudy.getClassId());
        classStudyDTO.setYear(classStudy.getYear());
        classStudyDTO.setTotalGrade(classStudy.getTotalGrade());
        return classStudyDTO;
    }
}
