package com.ems.ems_app.user_management.services;

import com.ems.ems_app.entities.Group;
import com.ems.ems_app.repos.GroupRepository;
import com.ems.ems_app.user_management.dto.requestDTO.StudentRequestDTO;
import com.ems.ems_app.user_management.dto.responseDTO.StudentResponseDTO;
import com.ems.ems_app.user_management.entities.Student;
import com.ems.ems_app.user_management.entities.User;
import com.ems.ems_app.user_management.exception.ClassStudyNotFoundException;
import com.ems.ems_app.user_management.exception.StudentNotFoundException;
import com.ems.ems_app.user_management.exception.UserNotFoundException;
import com.ems.ems_app.user_management.repos.StudentRepository;
import com.ems.ems_app.user_management.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, UserRepository userRepository, GroupRepository groupRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    public StudentResponseDTO createStudent(StudentRequestDTO studentRequestDTO) {

        User user = userRepository.findById(studentRequestDTO.getUserRequestDTO().getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + studentRequestDTO.getUserRequestDTO().getUserId()));

        Group group = groupRepository.findById(studentRequestDTO.getGroupId())
                .orElseThrow(() -> new ClassStudyNotFoundException("Group not found with id: " + studentRequestDTO.getGroupId()));


        Student student = new Student();
        student.setUser(user);
        student.setGroup(group);
        Student savedStudent = studentRepository.save(student);
        return StudentResponseDTO.convertToStudentResponseDTO(savedStudent);
    }

    public StudentResponseDTO getStudentById(UUID studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + studentId));
        return StudentResponseDTO.convertToStudentResponseDTO(student);
    }

    public List<StudentResponseDTO> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(StudentResponseDTO::convertToStudentResponseDTO)
                .collect(Collectors.toList());
    }

    public StudentResponseDTO updateStudent(UUID studentId, StudentRequestDTO studentRequestDTO) {
        Student existingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + studentId));

        User user = userRepository.findById(studentRequestDTO.getUserRequestDTO().getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + studentRequestDTO.getUserRequestDTO().getUserId()));

        Group group = groupRepository.findById(studentRequestDTO.getGroupId())
                .orElseThrow(() -> new ClassStudyNotFoundException("Group not found with id: " + studentRequestDTO.getGroupId()));

        existingStudent.setUser(user);
        existingStudent.setGroup(group);

        Student updatedStudent = studentRepository.update(existingStudent);
        return StudentResponseDTO.convertToStudentResponseDTO(updatedStudent);
    }

    public void deleteStudent(UUID studentId) {
        studentRepository.deleteById(studentId);
    }
}

