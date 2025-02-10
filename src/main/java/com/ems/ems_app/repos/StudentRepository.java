package com.ems.ems_app.repos;

import com.ems.ems_app.entities.ClassStudy;
import com.ems.ems_app.entities.Student;
import com.ems.ems_app.entities.User;
import com.ems.ems_app.exception.StudentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class StudentRepository {

    private final DataSource dataSource;
    private final UserRepository userRepository;
    private final ClassStudyRepository classStudyRepository;

    @Autowired
    public StudentRepository(DataSource dataSource, UserRepository userRepository, ClassStudyRepository classStudyRepository) {
        this.dataSource = dataSource;
        this.userRepository = userRepository;
        this.classStudyRepository = classStudyRepository;
    }

    public Student save(Student student) {
        String sql = "INSERT INTO Student (student_id, user_id, class_study_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            student.setStudentId(UUID.randomUUID());
            student.setCreatedAt(LocalDateTime.now());
            student.setUpdatedAt(LocalDateTime.now());

            preparedStatement.setObject(1, student.getStudentId());
            preparedStatement.setObject(2, student.getUser().getUserId());
            preparedStatement.setObject(3, student.getClassStudy().getClassId());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(student.getCreatedAt()));
            preparedStatement.setTimestamp(5, Timestamp.valueOf(student.getUpdatedAt()));

            preparedStatement.executeUpdate();
            return student;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save student", e);
        }
    }

    public Optional<Student> findById(UUID studentId) {
        String sql = "SELECT * FROM Student WHERE student_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, studentId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToStudent(resultSet));
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find student by id", e);
        }
    }

    public List<Student> findAll() {
        String sql = "SELECT * FROM Student";
        List<Student> students = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                students.add(mapResultSetToStudent(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all students", e);
        }

        return students;
    }

    public Student update(Student student) {
        String sql = "UPDATE Student SET user_id = ?, class_study_id = ?, updated_at = ? WHERE student_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, student.getUser().getUserId());
            preparedStatement.setObject(2, student.getClassStudy().getClassId());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setObject(4, student.getStudentId());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new StudentNotFoundException("Student not found with id: " + student.getStudentId());
            }

            return student;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update student", e);
        }
    }

    public void deleteById(UUID studentId) {
        String sql = "DELETE FROM Student WHERE student_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, studentId);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new StudentNotFoundException("Student not found with id: " + studentId);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete student", e);
        }
    }

    private Student mapResultSetToStudent(ResultSet resultSet) throws SQLException {
        Student student = new Student();
        student.setStudentId((UUID) resultSet.getObject("student_id"));

        // Fetch User and ClassStudy entities based on IDs
        UUID userId = (UUID) resultSet.getObject("user_id");
        User user = userRepository.findById(userId)
                .orElse(null); // Handle case where User might not exist

        UUID classStudyId = (UUID) resultSet.getObject("class_study_id");
        ClassStudy classStudy = classStudyRepository.findById(classStudyId)
                .orElse(null); // Handle case where ClassStudy might not exist

        student.setUser(user);
        student.setClassStudy(classStudy);
        student.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        student.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
        return student;
    }
}
