package com.ems.ems_app.repos;


import com.ems.ems_app.entities.Role;
import com.ems.ems_app.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class RoleRepository {

    private final DataSource dataSource;

    @Autowired
    public RoleRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public Optional<Role> findById(UUID id) {
        String sql = "SELECT role_id, description, details FROM role WHERE role_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Role role = new Role();
                    role.setRoleId((UUID) resultSet.getObject("role_id"));
                    role.setDescription(resultSet.getString("description"));
                    role.setDetails(resultSet.getString("details"));
                    return Optional.of(role);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find role by id", e);
        }
    }


    public List<Role> findAll() {
        String sql = "SELECT role_id, description, details FROM role";
        List<Role> roles = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Role role = new Role();
                role.setRoleId((UUID) resultSet.getObject("role_id"));
                role.setDescription(resultSet.getString("description"));
                role.setDetails(resultSet.getString("details"));
                roles.add(role);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all roles", e);
        }

        return roles;
    }


    public Role save(Role role) {
        if (role.getRoleId() == null) {
            String sql = "INSERT INTO role ( description, details) VALUES (?, ?)";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                role.setRoleId(UUID.randomUUID());
                preparedStatement.setString(1, role.getDescription());
                preparedStatement.setString(2, role.getDetails());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to save role", e);
            }
        } else {
            String sql = "UPDATE role SET  description = ?, details = ? WHERE role_id = ?";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, role.getDescription());
                preparedStatement.setString(2, role.getDetails());
                preparedStatement.setObject(3, role.getRoleId());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to update role", e);
            }
        }
        return role;
    }


    public void deleteById(UUID id) {
        String sql = "DELETE FROM role WHERE role_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, id);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new UserNotFoundException("Role not found with id: " + id); // Or a RoleNotFoundException
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete role", e);
        }
    }



}

