package com.ems.ems_app.repos;

import com.ems.ems_app.entities.AcademicYear;
import com.ems.ems_app.entities.AcademicYearGroup;
import com.ems.ems_app.entities.Group;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j // Optional: for logging errors
public class AcademicYearGroupRepository {

    private final DataSource dataSource; // Inject DataSource

    // SQL Statements
    private static final String INSERT_SQL = "INSERT INTO academic_year_groups (id, academic_year_id, group_id, year_number, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = "SELECT id, academic_year_id, group_id, year_number, created_at, updated_at FROM academic_year_groups WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, academic_year_id, group_id, year_number, created_at, updated_at FROM academic_year_groups";
    private static final String SELECT_BY_ACADEMIC_YEAR_SQL = "SELECT id, academic_year_id, group_id, year_number, created_at, updated_at FROM academic_year_groups WHERE academic_year_id = ?";
    private static final String UPDATE_SQL = "UPDATE academic_year_groups SET academic_year_id = ?, group_id = ?, year_number = ?, updated_at = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM academic_year_groups WHERE id = ?";

    public Optional<AcademicYearGroup> findById(UUID id) {
        // try-with-resources ensures Connection, PreparedStatement, ResultSet are closed
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setObject(1, id); // Set the UUID parameter
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToAcademicYearGroup(rs));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            log.error("Error finding AcademicYearGroup by id={}: {}", id, e.getMessage(), e);
            // Wrap checked SQLException in a RuntimeException
            throw new RuntimeException("Database error while finding AcademicYearGroup by ID", e);
        }
    }

    public List<AcademicYearGroup> findAll() {
        List<AcademicYearGroup> groups = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                groups.add(mapRowToAcademicYearGroup(rs));
            }
        } catch (SQLException e) {
            log.error("Error finding all AcademicYearGroups: {}", e.getMessage(), e);
            throw new RuntimeException("Database error while finding all AcademicYearGroups", e);
        }
        return groups;
    }

    public List<AcademicYearGroup> findByAcademicYearId(UUID academicYearId) {
        List<AcademicYearGroup> groups = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_ACADEMIC_YEAR_SQL)) {

            ps.setObject(1, academicYearId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    groups.add(mapRowToAcademicYearGroup(rs));
                }
            }
        } catch (SQLException e) {
            log.error("Error finding AcademicYearGroups by academicYearId={}: {}", academicYearId, e.getMessage(), e);
            throw new RuntimeException("Database error while finding AcademicYearGroups by Academic Year ID", e);
        }
        return groups;
    }


    public AcademicYearGroup save(AcademicYearGroup group) {
        if (group.getId() == null) {
            group.setId(UUID.randomUUID());
        }
        LocalDateTime now = LocalDateTime.now();
        group.setCreatedAt(now);
        group.setUpdatedAt(now);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_SQL)) {

            ps.setObject(1, group.getId());
            ps.setObject(2, group.getAcademicYear() != null ? group.getAcademicYear().getId() : null);
            ps.setObject(3, group.getGroup() != null ? group.getGroup().getId() : null);
            ps.setInt(4, group.getYearNumber());
            ps.setTimestamp(5, Timestamp.valueOf(group.getCreatedAt()));
            ps.setTimestamp(6, Timestamp.valueOf(group.getUpdatedAt()));

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Database error: Failed to save AcademicYearGroup, no rows affected.");
            }

        } catch (SQLException e) {
            log.error("Error saving AcademicYearGroup {}: {}", group, e.getMessage(), e);
            throw new RuntimeException("Database error while saving AcademicYearGroup", e);
        }
        return group; // Return the group with ID and timestamps
    }

    public Optional<AcademicYearGroup> update(AcademicYearGroup group) {
        if (group.getId() == null) {
            throw new IllegalArgumentException("Cannot update AcademicYearGroup with null ID");
        }
        group.setUpdatedAt(LocalDateTime.now());

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_SQL)) {

            ps.setObject(1, group.getAcademicYear() != null ? group.getAcademicYear().getId() : null);
            ps.setObject(2, group.getGroup() != null ? group.getGroup().getId() : null);
            ps.setInt(3, group.getYearNumber());
            ps.setTimestamp(4, Timestamp.valueOf(group.getUpdatedAt()));
            ps.setObject(5, group.getId()); // ID for the WHERE clause

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0 ? Optional.of(group) : Optional.empty(); // Return updated group if successful

        } catch (SQLException e) {
            log.error("Error updating AcademicYearGroup {}: {}", group, e.getMessage(), e);
            throw new RuntimeException("Database error while updating AcademicYearGroup", e);
        }
    }

    public boolean deleteById(UUID id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_SQL)) {

            ps.setObject(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            log.error("Error deleting AcademicYearGroup with id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Database error while deleting AcademicYearGroup", e);
        }
    }

    // Helper method to map a ResultSet row to an Entity
    private AcademicYearGroup mapRowToAcademicYearGroup(ResultSet rs) throws SQLException {
        AcademicYearGroup group = new AcademicYearGroup();
        group.setId(rs.getObject("id", UUID.class));

        // Create placeholder related entities with only IDs set
        AcademicYear academicYear = new AcademicYear();
        academicYear.setId(rs.getObject("academic_year_id", UUID.class)); // Assuming UUID type
        group.setAcademicYear(academicYear);

        Group groupEntity = new Group(); // Assuming 'Group' is your entity name
        groupEntity.setId(rs.getObject("group_id", UUID.class)); // Assuming UUID type
        group.setGroup(groupEntity);

        group.setYearNumber(rs.getInt("year_number"));

        Timestamp createdAtTs = rs.getTimestamp("created_at");
        group.setCreatedAt(createdAtTs != null ? createdAtTs.toLocalDateTime() : null);

        Timestamp updatedAtTs = rs.getTimestamp("updated_at");
        group.setUpdatedAt(updatedAtTs != null ? updatedAtTs.toLocalDateTime() : null);

        return group;
    }
}