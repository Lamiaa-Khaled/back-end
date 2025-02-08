package com.ems.ems_app.services;


import com.ems.ems_app.dto.StudentDTO;
import com.ems.ems_app.entities.ClassStudy;
import com.ems.ems_app.entities.Student;
import com.ems.ems_app.entities.User;
import com.ems.ems_app.exception.ResourceNotFoundException;
import com.ems.ems_app.repos.ClassStudyRepository;
import com.ems.ems_app.repos.StudentRepository;
import com.ems.ems_app.repos.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClassStudyRepository classStudyRepository;

    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return convertToDto(student);
    }

    public List<StudentDTO> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public StudentDTO createStudent(Student student) {
        //check if the user and classStudy exists before create student
        User user = userRepository.findById(student.getUser().getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + student.getUser().getUserId()));
        ClassStudy classStudy = classStudyRepository.findById(student.getClassStudy().getClassId()).orElseThrow(() -> new ResourceNotFoundException("ClassStudy not found with id: " + student.getClassStudy().getClassId()));

        student.setUser(user);
        student.setClassStudy(classStudy);
        Student savedStudent = studentRepository.save(student);
        return convertToDto(savedStudent);
    }

    public StudentDTO updateStudent(Long id, Student studentDetails) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        User user = userRepository.findById(studentDetails.getUser().getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + studentDetails.getUser().getUserId()));
        ClassStudy classStudy = classStudyRepository.findById(studentDetails.getClassStudy().getClassId()).orElseThrow(() -> new ResourceNotFoundException("ClassStudy not found with id: " + studentDetails.getClassStudy().getClassId()));

        student.setUser(user);
        student.setClassStudy(classStudy);
        Student updatedStudent = studentRepository.save(student);
        return convertToDto(updatedStudent);
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }

    private StudentDTO convertToDto(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setStudentId(student.getStudentId());
        studentDTO.setUserId(student.getUser().getUserId()); // Or map the User to UserDTO
        studentDTO.setClassStudyId(student.getClassStudy().getClassId()); // Or map ClassStudy to ClassStudyDTO
        return studentDTO;
    }

}