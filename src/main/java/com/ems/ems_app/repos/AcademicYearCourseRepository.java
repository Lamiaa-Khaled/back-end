package com.ems.ems_app.repos;

import com.ems.ems_app.entities.AcademicTerm;
import com.ems.ems_app.entities.AcademicYear;
import com.ems.ems_app.entities.AcademicYearCourse;
import com.ems.ems_app.entities.Course;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@Slf4j
public class AcademicYearCourseRepository {

    private final DataSource dataSource;

    // SQL Statements
    private static final String INSERT_SQL = "INSERT INTO academic_year_courses (id, academic_year_id, course_code, term_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = "SELECT id, academic_year_id, course_code, term_id, created_at, updated_at FROM academic_year_courses WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, academic_year_id, course_code, term_id, created_at, updated_at FROM academic_year_courses";
    private static final String UPDATE_SQL = "UPDATE academic_year_courses SET academic_year_id = ?, course_code = ?, term_id = ?, updated_at = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM academic_year_courses WHERE id = ?";
    // Add more SELECT statements if needed (e.g., find by academic year)

    public Optional<AcademicYearCourse> findById(UUID id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRowToAcademicYearCourse(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            log.error("Error finding AcademicYearCourse by id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Database error while finding AcademicYearCourse by ID", e);
        }
    }

    public List<AcademicYearCourse> findAll() {
        List<AcademicYearCourse> courses = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                courses.add(mapRowToAcademicYearCourse(rs));
            }
        } catch (SQLException e) {
            log.error("Error finding all AcademicYearCourses: {}", e.getMessage(), e);
            throw new RuntimeException("Database error while finding all AcademicYearCourses", e);
        }
        return courses;
    }

    public AcademicYearCourse save(AcademicYearCourse course) {
        if (course.getId() == null) {
            course.setId(UUID.randomUUID());
        }
        LocalDateTime now = LocalDateTime.now();
        course.setCreatedAt(now);
        course.setUpdatedAt(now);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_SQL)) {

            ps.setObject(1, course.getId());
            ps.setObject(2, course.getAcademicYear() != null ? course.getAcademicYear().getId() : null);
            ps.setString(3, course.getCourse() != null ? course.getCourse().getCode() : null); // Assuming Course uses code as identifier
            ps.setObject(4, course.getTerm() != null ? course.getTerm().getId() : null);
            ps.setTimestamp(5, Timestamp.valueOf(course.getCreatedAt()));
            ps.setTimestamp(6, Timestamp.valueOf(course.getUpdatedAt()));

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Database error: Failed to save AcademicYearCourse, no rows affected.");
            }
        } catch (SQLException e) {
            log.error("Error saving AcademicYearCourse {}: {}", course, e.getMessage(), e);
            throw new RuntimeException("Database error while saving AcademicYearCourse", e);
        }
        return course;
    }

    public Optional<AcademicYearCourse> update(AcademicYearCourse course) {
        if (course.getId() == null) {
            throw new IllegalArgumentException("Cannot update AcademicYearCourse with null ID");
        }
        course.setUpdatedAt(LocalDateTime.now());

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_SQL)) {

            ps.setObject(1, course.getAcademicYear() != null ? course.getAcademicYear().getId() : null);
            ps.setString(2, course.getCourse() != null ? course.getCourse().getCode() : null);
            ps.setObject(3, course.getTerm() != null ? course.getTerm().getId() : null);
            ps.setTimestamp(4, Timestamp.valueOf(course.getUpdatedAt()));
            ps.setObject(5, course.getId()); // ID for the WHERE clause

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0 ? Optional.of(course) : Optional.empty();

        } catch (SQLException e) {
            log.error("Error updating AcademicYearCourse {}: {}", course, e.getMessage(), e);
            throw new RuntimeException("Database error while updating AcademicYearCourse", e);
        }
    }

    public boolean deleteById(UUID id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_SQL)) {

            ps.setObject(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            log.error("Error deleting AcademicYearCourse with id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Database error while deleting AcademicYearCourse", e);
        }
    }

    private AcademicYearCourse mapRowToAcademicYearCourse(ResultSet rs) throws SQLException {
        AcademicYearCourse ayc = new AcademicYearCourse();
        ayc.setId(rs.getObject("id", UUID.class));

        AcademicYear ay = new AcademicYear();
        ay.setId(rs.getObject("academic_year_id", UUID.class));
        ayc.setAcademicYear(ay);

        Course c = new Course(); // Assuming Course entity exists
        c.setCode(rs.getString("course_code")); // Assuming Course PK is 'code' (String)
        ayc.setCourse(c);

        AcademicTerm term = new AcademicTerm(); // Assuming AcademicTerm entity exists
        term.setId(rs.getObject("term_id", UUID.class));
        ayc.setTerm(term);

        Timestamp createdAtTs = rs.getTimestamp("created_at");
        ayc.setCreatedAt(createdAtTs != null ? createdAtTs.toLocalDateTime() : null);

        Timestamp updatedAtTs = rs.getTimestamp("updated_at");
        ayc.setUpdatedAt(updatedAtTs != null ? updatedAtTs.toLocalDateTime() : null);

        return ayc;
    }
}