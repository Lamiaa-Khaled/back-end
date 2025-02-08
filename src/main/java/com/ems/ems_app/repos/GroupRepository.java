package com.ems.ems_app.repos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;

import com.ems.ems_app.exceptions.RepositoryException;
import org.springframework.stereotype.Repository;

import com.ems.ems_app.entities.Group;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class GroupRepository {

    private final DataSource dataSource;

    public GroupRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<Group> findById(UUID id) {
        String sql = "SELECT * FROM groups WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Group group = mapRowToGroup(rs);
                    return Optional.of(group);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error finding group by id: " + id, e);
        }
        return Optional.empty();
    }

    public List<Group> findAll() {
        String sql = "SELECT * FROM groups";
        List<Group> groups = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Group group = mapRowToGroup(rs);
                groups.add(group);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error finding all groups", e);
        }
        return groups;
    }

    public void save(Group group) {
        String sql = "INSERT INTO groups (id, name, description) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, group.getId());
            pstmt.setString(2, group.getName());
            pstmt.setString(3, group.getDescription());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error saving group: " + group.getId(), e);
        }
    }

    public void update(Group group) {
        String sql = "UPDATE groups SET name = ?, description = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, group.getName());
            pstmt.setString(2, group.getDescription());
            pstmt.setObject(3, group.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error updating group: " + group.getId(), e);
        }
    }
@Transactional
    public void deleteById(UUID id) {
        String sql = "DELETE FROM groups WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error deleting group: " + id, e);
        }
    }

    private Group mapRowToGroup(ResultSet rs) throws SQLException {
        Group group = new Group();
        group.setId((UUID) rs.getObject("id"));
        group.setName(rs.getString("name"));
        group.setDescription(rs.getString("description"));
        return group;
    }
}