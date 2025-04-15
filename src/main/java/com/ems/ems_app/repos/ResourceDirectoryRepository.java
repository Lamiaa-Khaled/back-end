package com.ems.ems_app.repos;
<<<<<<< HEAD

import com.ems.ems_app.entities.ResourceDirectory;
import com.ems.ems_app.exceptions.RepositoryException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
=======
import com.ems.ems_app.entities.Course;
import com.ems.ems_app.entities.ResourceDirectory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ResourceDirectoryRepository {
<<<<<<< HEAD

    private final DataSource dataSource;

    public ResourceDirectoryRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<ResourceDirectory> findById(UUID id) {
        String sql = "SELECT * FROM resource_directory WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    ResourceDirectory resourceDirectory = mapRowToResourceDirectory(rs);
                    return Optional.of(resourceDirectory);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error finding resource directory by id: " + id, e);
        }
        return Optional.empty();
    }

    public List<ResourceDirectory> findAll() {
        String sql = "SELECT * FROM resource_directory";
        List<ResourceDirectory> resourceDirectories = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                ResourceDirectory resourceDirectory = mapRowToResourceDirectory(rs);
                resourceDirectories.add(resourceDirectory);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error finding all resource directories", e);
        }
        return resourceDirectories;
    }

    public void save(ResourceDirectory resourceDirectory) {
        String sql = "INSERT INTO resource_directory (id, name, creator, base_dir_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, resourceDirectory.getId());
            pstmt.setString(2, resourceDirectory.getName());
            pstmt.setString(3, resourceDirectory.getCreator());
            pstmt.setObject(4, resourceDirectory.getBaseDirId());
            pstmt.setObject(5, resourceDirectory.getCreatedAt());
            pstmt.setObject(6, resourceDirectory.getUpdatedAt());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error saving resource directory: " + resourceDirectory.getId(), e);
        }
    }

    public void update(ResourceDirectory resourceDirectory) {
        String sql = "UPDATE resource_directory SET name = ?, creator = ?, base_dir_id = ?, updated_at = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, resourceDirectory.getName());
            pstmt.setString(2, resourceDirectory.getCreator());
            pstmt.setObject(3, resourceDirectory.getBaseDirId());
            pstmt.setObject(4, resourceDirectory.getUpdatedAt());
            pstmt.setObject(5, resourceDirectory.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error updating resource directory: " + resourceDirectory.getId(), e);
        }
    }

    public void deleteById(UUID id) {
        String sql = "DELETE FROM resource_directory WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error deleting resource directory: " + id, e);
        }
    }

    private ResourceDirectory mapRowToResourceDirectory(ResultSet rs) throws SQLException {
        ResourceDirectory resourceDirectory = new ResourceDirectory();
        resourceDirectory.setId((UUID) rs.getObject("id"));
        resourceDirectory.setName(rs.getString("name"));
        resourceDirectory.setCreator(rs.getString("creator"));
        resourceDirectory.setBaseDirId((UUID) rs.getObject("base_dir_id"));
        resourceDirectory.setCreatedAt(rs.getObject("created_at", java.time.LocalDateTime.class));
        resourceDirectory.setUpdatedAt(rs.getObject("updated_at", java.time.LocalDateTime.class));
        return resourceDirectory;
    }
=======
    @PersistenceContext
    private EntityManager entityManager;

    public List<ResourceDirectory> findAll() {
        return entityManager.createQuery("FROM ResourceDirectory", ResourceDirectory.class).getResultList();
    }

    public Optional<ResourceDirectory> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(ResourceDirectory.class, id));
    }

    @Transactional
    public ResourceDirectory save(ResourceDirectory resourceDirectory) {
        if (resourceDirectory.getId() == null) {
            resourceDirectory.setId(UUID.randomUUID());
            entityManager.persist(resourceDirectory);
        } else {
            entityManager.merge(resourceDirectory);
        }
        return resourceDirectory;
    }

    @Transactional
    public void deleteById(UUID id) {
        ResourceDirectory resourceDirectory = entityManager.find(ResourceDirectory.class, id);
        if (resourceDirectory != null) {
            entityManager.remove(resourceDirectory);
        }
    }
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
}
