package com.ems.ems_app.repos;

<<<<<<< HEAD
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;

import com.ems.ems_app.entities.Group;
import com.ems.ems_app.exceptions.RepositoryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import com.ems.ems_app.entities.Course;
import com.ems.ems_app.entities.ResourceDirectory;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
@Repository
public class CourseRepository {

    private final DataSource dataSource;
    LocalDateTime now = LocalDateTime.now();

    public CourseRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<Course> findById(String code) {
        String sql = "SELECT * FROM course WHERE code = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Course course = mapRowToCourse(rs);
                    return Optional.of(course);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error finding course by code: " + code, e);
        }
        return Optional.empty();
    }

    public List<Course> findAll() {
        String sql = "SELECT * FROM course";
        List<Course> courses = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Course course = mapRowToCourse(rs);
                courses.add(course);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error finding all courses", e);
        }
        return courses;
    }
@Transactional
    public void save(Course course) {
        String sql = "INSERT INTO course (code, name, avatar_id, active, group_id, created_at, updated_at, dir_doc_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, course.getCode());
            pstmt.setString(2, course.getName());
            pstmt.setObject(3, course.getAvatarId());
            pstmt.setBoolean(4, course.isActive());
            pstmt.setObject(5, course.getGroup().getId());
            pstmt.setObject(6, now);
            pstmt.setObject(7, now);
            pstmt.setObject(8, course.getBaseDirectory().getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error saving course: " + course.getCode(), e);
        }
    }
@Transactional
    public void update(Course course) {
        String sql = "UPDATE course SET name = ?, avatar_id = ?, active = ?, group_id = ?, updated_at = ?, dir_doc_id = ? " +
                "WHERE code = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, course.getName());
            pstmt.setObject(2, course.getAvatarId());
            pstmt.setBoolean(3, course.isActive());
            pstmt.setObject(4, course.getGroup().getId());
            pstmt.setObject(5, now);
            pstmt.setObject(6, course.getBaseDirectory().getId());
            pstmt.setString(7, course.getCode());


            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error updating course: " + course.getCode(), e);
        }
    }

    public void deleteById(String code) {
        String sql = "DELETE FROM course WHERE code = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error deleting course: " + code, e);
        }
    }
    private static final String SELECT_BY_CODE_SQL = """
            SELECT  code, name
            FROM course
            WHERE code = ?
            """;
    public Optional<Course> findByCode(String code) {
        log.debug("Attempting to find course by code: {}", code);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_CODE_SQL)) {

            ps.setString(1, code); // Set the code parameter
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Found a course, map it and return
                    Course course = mapRow(rs);
                    log.debug("Found course: {}", course);
                    return Optional.of(course);
                } else {
                    // No course found with this code
                    log.debug("No course found with code: {}", code);
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            log.error("Database error finding Course by code={}: {}", code, e.getMessage(), e);
            // Re-throw as a runtime exception to signal a layer failure
            throw new RuntimeException("Database error while finding Course by code", e);
        }
    }

    private Course mapRow(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setCode(rs.getString("code"));
        return course;
    }





    private Course mapRowToCourse(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setCode(rs.getString("code"));
        course.setName(rs.getString("name"));
        course.setAvatarId((UUID) rs.getObject("avatar_id"));
        course.setActive(rs.getBoolean("active"));

        // Assuming you have a GroupRepository to fetch the Group object by ID:
        // IMPORTANT:  You'll need to inject GroupRepository into CourseRepository
        Group group = new Group();
        UUID groupId = (UUID) rs.getObject("group_id");
        group.setId(groupId);
        course.setGroup(group); //Fetch the group
        course.setCreatedAt(rs.getObject("created_at", java.time.LocalDateTime.class));
        course.setUpdatedAt(rs.getObject("updated_at", java.time.LocalDateTime.class));
        ResourceDirectory baseDirectory = new ResourceDirectory();
        UUID resourceDirectoryId = (UUID) rs.getObject("dir_doc_id");
        baseDirectory.setId(resourceDirectoryId);
        course.setBaseDirectory(baseDirectory);

        return course;
    }
}
=======
import com.ems.ems_app.entities.Course;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CourseRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Course> findAll() {
        return entityManager.createQuery("FROM Course", Course.class).getResultList();
    }

    public Optional<Course> findById(String code) {
        return Optional.ofNullable(entityManager.find(Course.class, code));
    }

    @Transactional
    public Course save(Course course) {
        if (course.getCode() == null) {
            throw new IllegalArgumentException("Course code cannot be null");
        }
        if (entityManager.find(Course.class, course.getCode()) == null) {
            entityManager.persist(course);
        } else {
            entityManager.merge(course);
        }
        return course;
    }

    @Transactional
    public void deleteById(String code) {
        Course course = entityManager.find(Course.class, code);
        if (course != null) {
            entityManager.remove(course);
        }
    }
}
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
