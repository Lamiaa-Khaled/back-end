package com.ems.ems_app.repos;

import com.ems.ems_app.entities.AcademicYear;
import com.ems.ems_app.entities.AcademicYearCourseAdmin;
import com.ems.ems_app.entities.Course;
import com.ems.ems_app.user_management.entities.Admin;
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
@Slf4j
public class AcademicYearCourseAdminRepository {

    private final DataSource dataSource;

    // SQL Statements
    private static final String INSERT_SQL = "INSERT INTO academic_year_course_admins (id, academic_year_id, course_code, admin_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = "SELECT id, academic_year_id, course_code, admin_id, created_at, updated_at FROM academic_year_course_admins WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, academic_year_id, course_code, admin_id, created_at, updated_at FROM academic_year_course_admins";
    private static final String UPDATE_SQL = "UPDATE academic_year_course_admins SET academic_year_id = ?, course_code = ?, admin_id = ?, updated_at = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM academic_year_course_admins WHERE id = ?";
    // Add more SELECTs as needed

    public Optional<AcademicYearCourseAdmin> findById(UUID id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRowToAcademicYearCourseAdmin(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            log.error("Error finding AcademicYearCourseAdmin by id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Database error while finding AcademicYearCourseAdmin by ID", e);
        }
    }

    public List<AcademicYearCourseAdmin> findAll() {
        List<AcademicYearCourseAdmin> admins = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                admins.add(mapRowToAcademicYearCourseAdmin(rs));
            }
        } catch (SQLException e) {
            log.error("Error finding all AcademicYearCourseAdmins: {}", e.getMessage(), e);
            throw new RuntimeException("Database error while finding all AcademicYearCourseAdmins", e);
        }
        return admins;
    }

    public AcademicYearCourseAdmin save(AcademicYearCourseAdmin admin) {
        if (admin.getId() == null) {
            admin.setId(UUID.randomUUID());
        }
        LocalDateTime now = LocalDateTime.now();
        admin.setCreatedAt(now);
        admin.setUpdatedAt(now);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_SQL)) {

            ps.setObject(1, admin.getId());
            ps.setObject(2, admin.getAcademicYear() != null ? admin.getAcademicYear().getId() : null);
            ps.setString(3, admin.getCourse() != null ? admin.getCourse().getCode() : null);
            ps.setObject(4, admin.getAdmin() != null ? admin.getAdmin().getAdminId() : null); // Assuming Admin uses UUID id
            ps.setTimestamp(5, Timestamp.valueOf(admin.getCreatedAt()));
            ps.setTimestamp(6, Timestamp.valueOf(admin.getUpdatedAt()));

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Database error: Failed to save AcademicYearCourseAdmin, no rows affected.");
            }
        } catch (SQLException e) {
            log.error("Error saving AcademicYearCourseAdmin {}: {}", admin, e.getMessage(), e);
            throw new RuntimeException("Database error while saving AcademicYearCourseAdmin", e);
        }
        return admin;
    }

    public Optional<AcademicYearCourseAdmin> update(AcademicYearCourseAdmin admin) {
        if (admin.getId() == null) {
            throw new IllegalArgumentException("Cannot update AcademicYearCourseAdmin with null ID");
        }
        admin.setUpdatedAt(LocalDateTime.now());

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_SQL)) {

            ps.setObject(1, admin.getAcademicYear() != null ? admin.getAcademicYear().getId() : null);
            ps.setString(2, admin.getCourse() != null ? admin.getCourse().getCode() : null);
            ps.setObject(3, admin.getAdmin() != null ? admin.getAdmin().getAdminId(): null);
            ps.setTimestamp(4, Timestamp.valueOf(admin.getUpdatedAt()));
            ps.setObject(5, admin.getId()); // ID for the WHERE clause

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0 ? Optional.of(admin) : Optional.empty();

        } catch (SQLException e) {
            log.error("Error updating AcademicYearCourseAdmin {}: {}", admin, e.getMessage(), e);
            throw new RuntimeException("Database error while updating AcademicYearCourseAdmin", e);
        }
    }

    public boolean deleteById(UUID id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_SQL)) {

            ps.setObject(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            log.error("Error deleting AcademicYearCourseAdmin with id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Database error while deleting AcademicYearCourseAdmin", e);
        }
    }

    private AcademicYearCourseAdmin mapRowToAcademicYearCourseAdmin(ResultSet rs) throws SQLException {
        AcademicYearCourseAdmin ayca = new AcademicYearCourseAdmin();
        ayca.setId(rs.getObject("id", UUID.class));

        AcademicYear ay = new AcademicYear();
        ay.setId(rs.getObject("academic_year_id", UUID.class));
        ayca.setAcademicYear(ay);

        Course c = new Course();
        c.setCode(rs.getString("course_code"));
        ayca.setCourse(c);

        Admin admin = new Admin(); // Assuming Admin entity exists
        admin.setAdminId(rs.getObject("admin_id", UUID.class)); // Assuming Admin PK is 'id' (UUID)
        ayca.setAdmin(admin);
        Timestamp createdAtTs = rs.getTimestamp("created_at");
        ayca.setCreatedAt(createdAtTs != null ? createdAtTs.toLocalDateTime() : null);

        Timestamp updatedAtTs = rs.getTimestamp("updated_at");
        ayca.setUpdatedAt(updatedAtTs != null ? updatedAtTs.toLocalDateTime() : null);

        return ayca;
    }
}
