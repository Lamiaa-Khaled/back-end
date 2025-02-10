package com.ems.ems_app.services;

import com.ems.ems_app.dto.requestDTO.StudentRequestDTO;
import com.ems.ems_app.dto.responseDTO.StudentResponseDTO;
import com.ems.ems_app.entities.ClassStudy;
import com.ems.ems_app.entities.Student;
import com.ems.ems_app.entities.User;
import com.ems.ems_app.exception.ClassStudyNotFoundException;
import com.ems.ems_app.exception.StudentNotFoundException;
import com.ems.ems_app.exception.UserNotFoundException;
import com.ems.ems_app.repos.ClassStudyRepository;
import com.ems.ems_app.repos.StudentRepository;
import com.ems.ems_app.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final ClassStudyRepository classStudyRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, UserRepository userRepository, ClassStudyRepository classStudyRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.classStudyRepository = classStudyRepository;
    }

    public StudentResponseDTO createStudent(StudentRequestDTO studentRequestDTO) {

        User user = userRepository.findById(studentRequestDTO.getUserRequestDTO().getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + studentRequestDTO.getUserRequestDTO().getUserId()));

        ClassStudy classStudy = classStudyRepository.findById(studentRequestDTO.getClassStudyId())
                .orElseThrow(() -> new ClassStudyNotFoundException("ClassStudy not found with id: " + studentRequestDTO.getClassStudyId()));


        Student student = new Student();
        student.setUser(user);
        student.setClassStudy(classStudy);
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

        ClassStudy classStudy = classStudyRepository.findById(studentRequestDTO.getClassStudyId())
                .orElseThrow(() -> new ClassStudyNotFoundException("ClassStudy not found with id: " + studentRequestDTO.getClassStudyId()));

        existingStudent.setUser(user);
        existingStudent.setClassStudy(classStudy);

        Student updatedStudent = studentRepository.update(existingStudent);
        return StudentResponseDTO.convertToStudentResponseDTO(updatedStudent);
    }

    public void deleteStudent(UUID studentId) {
        studentRepository.deleteById(studentId);
    }
}

