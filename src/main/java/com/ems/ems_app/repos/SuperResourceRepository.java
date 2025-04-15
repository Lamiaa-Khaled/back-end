package com.ems.ems_app.repos;
<<<<<<< HEAD

import com.ems.ems_app.entities.Resource;
import com.ems.ems_app.entities.SuperResource;
import com.ems.ems_app.exceptions.RepositoryException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
=======
import com.ems.ems_app.entities.Course;
import com.ems.ems_app.entities.SuperResource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
import java.util.Optional;
import java.util.UUID;

@Repository
public class SuperResourceRepository {
<<<<<<< HEAD

    private final DataSource dataSource;

    public SuperResourceRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<SuperResource> findById(UUID id) {
        String sql = "SELECT * FROM super_resource WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    SuperResource superResource = mapRowToSuperResource(rs);
                    return Optional.of(superResource);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error finding super resource by id: " + id, e);
        }
        return Optional.empty();
    }

    public void save(SuperResource superResource) {
        String sql = "INSERT INTO super_resource (id, data, resource_id) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, superResource.getId());
            pstmt.setBytes(2, superResource.getData());
            pstmt.setObject(3, superResource.getResource().getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error saving super resource: " + superResource.getId(), e);
        }
    }

    public void update(SuperResource superResource) {
        String sql = "UPDATE super_resource SET data = ?, resource_id = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBytes(1, superResource.getData());
            pstmt.setObject(2, superResource.getResource().getId());
            pstmt.setObject(3, superResource.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error updating super resource: " + superResource.getId(), e);
        }
    }

    public void deleteById(UUID id) {
        String sql = "DELETE FROM super_resource WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error deleting super resource: " + id, e);
        }
    }

    private SuperResource mapRowToSuperResource(ResultSet rs) throws SQLException {
        SuperResource superResource = new SuperResource();
        superResource.setId((UUID) rs.getObject("id"));
        superResource.setData(rs.getBytes("data"));
        Resource resource = new Resource();
        UUID resourceId = (UUID) rs.getObject("resource_id");
        resource.setId(resourceId);
        superResource.setResource(resource);
        return superResource;
    }
}
=======
    @PersistenceContext
    private EntityManager entityManager;

    public List<SuperResource> findAll() {
        return entityManager.createQuery("FROM SuperResource", SuperResource.class).getResultList();
    }

    public Optional<SuperResource> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(SuperResource.class, id));
    }

    @Transactional
    public SuperResource save(SuperResource superResource) {
        if (superResource.getId() == null) {
            superResource.setId(UUID.randomUUID());
            entityManager.persist(superResource);
        } else {
            entityManager.merge(superResource);
        }
        return superResource;
    }

    @Transactional
    public void deleteById(UUID id) {
        SuperResource superResource = entityManager.find(SuperResource.class, id);
        if (superResource != null) {
            entityManager.remove(superResource);
        }
    }
}
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
