package com.ems.ems_app.repos;



import com.ems.ems_app.entities.User;
import com.ems.ems_app.exception.UserNotFoundException;
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
public class UserRepository {

    private final DataSource dataSource;

    @Autowired
    public UserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public User save(User user) {
        String sql = "INSERT INTO Users (user_id, first_name, last_name, email, password, user_type, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            user.setUserId(UUID.randomUUID());
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());

            preparedStatement.setObject(1, user.getUserId());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setString(6, user.getUserType());
            preparedStatement.setTimestamp(7, Timestamp.valueOf(user.getCreatedAt()));
            preparedStatement.setTimestamp(8, Timestamp.valueOf(user.getUpdatedAt()));

            preparedStatement.executeUpdate();

            return user;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save user", e); // Or a more specific exception
        }
    }

    public Optional<User> findById(UUID userId) {
        String sql = "SELECT * FROM Users WHERE user_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToUser(resultSet));
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user by id", e);
        }
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE email = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToUser(resultSet));
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user by email", e);
        }
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM Users";

        List<User> users = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                users.add(mapResultSetToUser(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all users", e);
        }

        return users;
    }

    public User update(User user) {
        String sql = "UPDATE Users SET first_name = ?, last_name = ?, email = ?, password = ?, user_type = ?, updated_at = ? WHERE user_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getUserType());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now())); // Update the updated_at timestamp
            preparedStatement.setObject(7, user.getUserId());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new UserNotFoundException("User not found with id: " + user.getUserId());
            }

            return user;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update user", e);
        }
    }

    public void deleteById(UUID userId) {
        String sql = "DELETE FROM Users WHERE user_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, userId);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new UserNotFoundException("User not found with id: " + userId);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setUserId((UUID) resultSet.getObject("user_id"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setUserType(resultSet.getString("user_type"));
        user.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        user.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
        return user;
    }
}