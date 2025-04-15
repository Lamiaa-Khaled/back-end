package com.ems.ems_app.repos;
<<<<<<< HEAD

import com.ems.ems_app.entities.Resource;
import com.ems.ems_app.entities.ResourceDirectory;
import com.ems.ems_app.exceptions.RepositoryException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
=======
import com.ems.ems_app.entities.Course;
import com.ems.ems_app.entities.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ResourceRepository {
<<<<<<< HEAD

    private final DataSource dataSource;

    public ResourceRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<Resource> findById(UUID id) {
        String sql = "SELECT * FROM resource WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Resource resource = mapRowToResource(rs);
                    return Optional.of(resource);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error finding resource by id: " + id, e);
        }
        return Optional.empty();
    }

    public List<Resource> findAll() {
        String sql = "SELECT * FROM resource";
        List<Resource> resources = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Resource resource = mapRowToResource(rs);
                resources.add(resource);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error finding all resources", e);
        }
        return resources;
    }

    public void save(Resource resource) {
        String sql = "INSERT INTO resource (id, name, type, resource_dir_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, resource.getId());
            pstmt.setString(2, resource.getName());
            pstmt.setString(3, resource.getType());
            pstmt.setObject(4, resource.getResourceDirectory().getId());
            pstmt.setObject(5, resource.getCreatedAt());
            pstmt.setObject(6, resource.getUpdatedAt());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error saving resource: " + resource.getId(), e);
        }
    }

    public void update(Resource resource) {
        String sql = "UPDATE resource SET name = ?, type = ?, resource_dir_id = ?, updated_at = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, resource.getName());
            pstmt.setString(2, resource.getType());
            pstmt.setObject(3, resource.getResourceDirectory().getId());
            pstmt.setObject(4, resource.getUpdatedAt());
            pstmt.setObject(5, resource.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error updating resource: " + resource.getId(), e);
        }
    }

    public void deleteById(UUID id) {
        String sql = "DELETE FROM resource WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error deleting resource: " + id, e);
        }
    }

    private Resource mapRowToResource(ResultSet rs) throws SQLException {
        Resource resource = new Resource();
        resource.setId((UUID) rs.getObject("id"));
        resource.setName(rs.getString("name"));
        resource.setType(rs.getString("type"));

        // Assuming you have a ResourceDirectoryRepository to fetch the ResourceDirectory object by ID:
        ResourceDirectory resourceDirectory = new ResourceDirectory();
        UUID resourceDirId = (UUID) rs.getObject("resource_dir_id");
        resourceDirectory.setId(resourceDirId);
        resource.setResourceDirectory(resourceDirectory);
        resource.setCreatedAt(rs.getObject("created_at", java.time.LocalDateTime.class));
        resource.setUpdatedAt(rs.getObject("updated_at", java.time.LocalDateTime.class));
        return resource;
    }
}

=======
    @PersistenceContext
    private EntityManager entityManager;

    public List<Resource> findAll() {
        return entityManager.createQuery("FROM Resource", Resource.class).getResultList();
    }

    public Optional<Resource> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(Resource.class, id));
    }

    @Transactional
    public Resource save(Resource resource) {
        if (resource.getId() == null) {
            resource.setId(UUID.randomUUID());
            entityManager.persist(resource);
        } else {
            entityManager.merge(resource);
        }
        return resource;
    }

    @Transactional
    public void deleteById(UUID id) {
        Resource resource = entityManager.find(Resource.class, id);
        if (resource != null) {
            entityManager.remove(resource);
        }
    }
}
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
