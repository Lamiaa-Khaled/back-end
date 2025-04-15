package com.ems.ems_app.user_management.repos;

import com.ems.ems_app.user_management.entities.Specialization;
import com.ems.ems_app.user_management.exception.SpecializationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SpecializationRepository {

    private final DataSource dataSource;

    @Autowired
    public SpecializationRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Specialization save(Specialization specialization) {
        String sql = "INSERT INTO Specialization (specialization_id, specialization_name) VALUES (?, ?)";
        System.out.println("Repository layer: " + specialization.getSpecializationName());
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            specialization.setSpecializationId(UUID.randomUUID());

            preparedStatement.setObject(1, specialization.getSpecializationId());
            preparedStatement.setString(2, specialization.getSpecializationName());

            preparedStatement.executeUpdate();
            return specialization;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save specialization", e);
        }
    }

    public Optional<Specialization> findById(UUID specializationId) {
        String sql = "SELECT * FROM Specialization WHERE specialization_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, specializationId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToSpecialization(resultSet));
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find specialization by id", e);
        }
    }

    public List<Specialization> findAll() {
        String sql = "SELECT * FROM Specialization";
        List<Specialization> specializations = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                specializations.add(mapResultSetToSpecialization(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all specializations", e);
        }

        return specializations;
    }

    public Specialization update(Specialization specialization) {
        String sql = "UPDATE Specialization SET specialization_name = ? WHERE specialization_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, specialization.getSpecializationName());
            preparedStatement.setObject(2, specialization.getSpecializationId());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SpecializationNotFoundException("Specialization not found with id: " + specialization.getSpecializationId());
            }

            return specialization;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update specialization", e);
        }
    }

    public void deleteById(UUID specializationId) {
        String sql = "DELETE FROM Specialization WHERE specialization_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, specializationId);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new SpecializationNotFoundException("Specialization not found with id: " + specializationId);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete specialization", e);
        }
    }

    private Specialization mapResultSetToSpecialization(ResultSet resultSet) throws SQLException {
        Specialization specialization = new Specialization();
        specialization.setSpecializationId((UUID) resultSet.getObject("specialization_id"));
        specialization.setSpecializationName(resultSet.getString("specialization_name"));
        return specialization;
    }
}