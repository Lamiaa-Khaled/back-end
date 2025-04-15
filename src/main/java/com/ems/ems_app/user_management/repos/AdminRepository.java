package com.ems.ems_app.user_management.repos;

import com.ems.ems_app.user_management.entities.Admin;
import com.ems.ems_app.user_management.entities.Specialization;
import com.ems.ems_app.user_management.entities.User;
import com.ems.ems_app.user_management.exception.AdminNotFoundException;
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
public class AdminRepository {

    private final DataSource dataSource;
    private final UserRepository userRepository;
    private final SpecializationRepository specializationRepository;

    @Autowired
    public AdminRepository(DataSource dataSource, UserRepository userRepository, SpecializationRepository specializationRepository) {
        this.dataSource = dataSource;
        this.userRepository = userRepository;
        this.specializationRepository = specializationRepository;
    }

    public Admin save(Admin admin) {
        String sql = "INSERT INTO Admin (admin_id, user_id, specialization_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            admin.setAdminId(UUID.randomUUID());
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());

            preparedStatement.setObject(1, admin.getAdminId());
            preparedStatement.setObject(2, admin.getUser().getUserId());
            preparedStatement.setObject(3, admin.getSpecialization().getSpecializationId());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(admin.getCreatedAt()));
            preparedStatement.setTimestamp(5, Timestamp.valueOf(admin.getUpdatedAt()));

            preparedStatement.executeUpdate();
            return admin;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save admin", e);
        }
    }

    public Optional<Admin> findById(UUID adminId) {
        String sql = "SELECT * FROM Admin WHERE admin_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, adminId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToAdmin(resultSet));
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find admin by id", e);
        }
    }

    public List<Admin> findAll() {
        String sql = "SELECT * FROM Admin";
        List<Admin> admins = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                admins.add(mapResultSetToAdmin(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all admins", e);
        }

        return admins;
    }

    public Admin update(Admin admin) {
        String sql = "UPDATE Admin SET user_id = ?, specialization_id = ?, updated_at = ? WHERE admin_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, admin.getUser().getUserId());
            preparedStatement.setObject(2, admin.getSpecialization().getSpecializationId());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setObject(4, admin.getAdminId());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new AdminNotFoundException("Admin not found with id: " + admin.getAdminId());
            }

            return admin;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update admin", e);
        }
    }

    public void deleteById(UUID adminId) {
        String sql = "DELETE FROM Admin WHERE admin_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, adminId);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new AdminNotFoundException("Admin not found with id: " + adminId);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete admin", e);
        }
    }

    private Admin mapResultSetToAdmin(ResultSet resultSet) throws SQLException {
        Admin admin = new Admin();
        admin.setAdminId((UUID) resultSet.getObject("admin_id"));

        UUID userId = (UUID) resultSet.getObject("user_id");
        User user = userRepository.findById(userId)
                .orElse(null); // Handle case where User might not exist

        UUID specializationId = (UUID) resultSet.getObject("specialization_id");
        Specialization specialization = specializationRepository.findById(specializationId)
                .orElse(null); // Handle case where Specialization might not exist

        admin.setUser(user);
        admin.setSpecialization(specialization);
        admin.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        admin.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
        return admin;
    }
}
