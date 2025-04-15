package com.ems.ems_app.user_management.repos;

import com.ems.ems_app.user_management.entities.Privilege;
import com.ems.ems_app.user_management.exception.UserNotFoundException;
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
public class PrivilegeRepository {

    private final DataSource dataSource;

    @Autowired
    public PrivilegeRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public Optional<Privilege> findById(UUID id) {
        String sql = "SELECT id, description, userId, roleId FROM privilege WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Privilege privilege = new Privilege();
                    privilege.setId((UUID) resultSet.getObject("id"));
                    privilege.setDescription(resultSet.getString("description"));
                    privilege.setUserId((UUID) resultSet.getObject("userId"));
                    privilege.setRoleId((UUID) resultSet.getObject("roleId"));
                    return Optional.of(privilege);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find privilege by id", e);
        }
    }

    public List<Privilege> findAll() {
        String sql = "SELECT id, description, userId, roleId FROM privilege";
        List<Privilege> privileges = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Privilege privilege = new Privilege();
                privilege.setId((UUID) resultSet.getObject("id"));
                privilege.setDescription(resultSet.getString("description"));
                privilege.setUserId((UUID) resultSet.getObject("userId"));
                privilege.setRoleId((UUID) resultSet.getObject("roleId"));
                privileges.add(privilege);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all privileges", e);
        }

        return privileges;
    }


    public Privilege save(Privilege privilege) {
        if (privilege.getId() == null) {
            String sql = "INSERT INTO privilege ( description, userId, roleId) VALUES (?, ?, ?)";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                privilege.setId(UUID.randomUUID());
                preparedStatement.setString(1, privilege.getDescription());
                preparedStatement.setObject(2,privilege.getUserId());
                preparedStatement.setObject(3,privilege.getRoleId());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to save privilege", e);
            }
        } else {
            String sql = "UPDATE privilege SET  description = ?, userId = ?, roleId = ? WHERE id = ?";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, privilege.getDescription());
                preparedStatement.setObject(2,privilege.getUserId());
                preparedStatement.setObject(3,privilege.getRoleId());
                preparedStatement.setObject(4, privilege.getId());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to update privilege", e);
            }
        }
        return privilege;
    }

    public void deleteById(UUID id) {
        String sql = "DELETE FROM privilege WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, id);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new UserNotFoundException("Privilege not found with id: " + id); // Or a RoleNotFoundException
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete privilege", e);
        }
    }


}
