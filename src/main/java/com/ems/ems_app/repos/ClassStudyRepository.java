package com.ems.ems_app.repos;

import com.ems.ems_app.entities.ClassStudy;
import com.ems.ems_app.exception.ClassStudyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ClassStudyRepository {

    private final DataSource dataSource;

    @Autowired
    public ClassStudyRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ClassStudy save(ClassStudy classStudy) {
        String sql = "INSERT INTO ClassStudy (class_id, year, total_grade, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            classStudy.setClassId(UUID.randomUUID());
            classStudy.setCreatedAt(LocalDateTime.now());
            classStudy.setUpdatedAt(LocalDateTime.now());

            preparedStatement.setObject(1, classStudy.getClassId());
            preparedStatement.setInt(2, classStudy.getYear());
            preparedStatement.setBigDecimal(3, classStudy.getTotalGrade());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(classStudy.getCreatedAt()));
            preparedStatement.setTimestamp(5, Timestamp.valueOf(classStudy.getUpdatedAt()));

            preparedStatement.executeUpdate();

            return classStudy;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save class study", e);
        }
    }

    public Optional<ClassStudy> findById(UUID classId) {
        String sql = "SELECT * FROM ClassStudy WHERE class_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, classId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToClassStudy(resultSet));
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find class study by id", e);
        }
    }

    public List<ClassStudy> findAll() {
        String sql = "SELECT * FROM ClassStudy";

        List<ClassStudy> classStudies = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                classStudies.add(mapResultSetToClassStudy(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all class studies", e);
        }

        return classStudies;
    }

    public ClassStudy update(ClassStudy classStudy) {
        String sql = "UPDATE ClassStudy SET year = ?, total_grade = ?, updated_at = ? WHERE class_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, classStudy.getYear());
            preparedStatement.setBigDecimal(2, classStudy.getTotalGrade());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setObject(4, classStudy.getClassId());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new ClassStudyNotFoundException("ClassStudy not found with id: " + classStudy.getClassId());
            }

            return classStudy;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update class study", e);
        }
    }

    public void deleteById(UUID classId) {
        String sql = "DELETE FROM ClassStudy WHERE class_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, classId);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new ClassStudyNotFoundException("ClassStudy not found with id: " + classId);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete class study", e);
        }
    }

    private ClassStudy mapResultSetToClassStudy(ResultSet resultSet) throws SQLException {
        ClassStudy classStudy = new ClassStudy();
        classStudy.setClassId((UUID) resultSet.getObject("class_id"));
        classStudy.setYear(resultSet.getInt("year"));
        classStudy.setTotalGrade(resultSet.getBigDecimal("total_grade"));
        classStudy.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        classStudy.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
        return classStudy;
    }
}
