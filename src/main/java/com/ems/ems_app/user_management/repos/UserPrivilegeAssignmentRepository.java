package com.ems.ems_app.user_management.repos;

import com.ems.ems_app.user_management.entities.UserPrivilegeAssignment;
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
public class UserPrivilegeAssignmentRepository {

    private final DataSource dataSource;

    @Autowired
    public UserPrivilegeAssignmentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public Optional<UserPrivilegeAssignment> findById(UUID id) {
        String sql = "SELECT Id, userId, privilegeId FROM user_privilege_assignment WHERE Id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    UserPrivilegeAssignment userPrivilegeAssignment = new UserPrivilegeAssignment();
                    userPrivilegeAssignment.setId((UUID) resultSet.getObject("Id"));
                    userPrivilegeAssignment.setUserId((UUID) resultSet.getObject("userId"));
                    userPrivilegeAssignment.setPrivilegeId((UUID) resultSet.getObject("privilegeId"));
                    return Optional.of(userPrivilegeAssignment);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find UserPrivilegeAssignment by id", e);
        }
    }

    public List<UserPrivilegeAssignment> findAll() {
        String sql = "SELECT Id, userId, privilegeId FROM user_privilege_assignment";
        List<UserPrivilegeAssignment> userPrivilegeAssignments = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                UserPrivilegeAssignment userPrivilegeAssignment = new UserPrivilegeAssignment();
                userPrivilegeAssignment.setId((UUID) resultSet.getObject("Id"));
                userPrivilegeAssignment.setUserId((UUID) resultSet.getObject("userId"));
                userPrivilegeAssignment.setPrivilegeId((UUID) resultSet.getObject("privilegeId"));
                userPrivilegeAssignments.add(userPrivilegeAssignment);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all UserPrivilegeAssignments", e);
        }

        return userPrivilegeAssignments;
    }


    public UserPrivilegeAssignment save(UserPrivilegeAssignment userPrivilegeAssignment) {
        if (userPrivilegeAssignment.getId() == null) {
            String sql = "INSERT INTO user_privilege_assignment ( userId, privilegeId) VALUES (?, ?)";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                userPrivilegeAssignment.setId(UUID.randomUUID());
                preparedStatement.setObject(1, userPrivilegeAssignment.getUserId());
                preparedStatement.setObject(2, userPrivilegeAssignment.getPrivilegeId());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to save UserPrivilegeAssignment", e);
            }
        } else {
            String sql = "UPDATE user_privilege_assignment SET  userId = ?, privilegeId = ? WHERE Id = ?";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setObject(1, userPrivilegeAssignment.getUserId());
                preparedStatement.setObject(2, userPrivilegeAssignment.getPrivilegeId());
                preparedStatement.setObject(3, userPrivilegeAssignment.getId());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to update UserPrivilegeAssignment", e);
            }
        }
        return userPrivilegeAssignment;
    }


    public void deleteById(UUID id) {
        String sql = "DELETE FROM user_privilege_assignment WHERE Id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, id);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new UserNotFoundException("UserPrivilegeAssignment not found with id: " + id); // Or a RoleNotFoundException
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete UserPrivilegeAssignment", e);
        }
    }


}
