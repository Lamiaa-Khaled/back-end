package com.ems.ems_app.repos;

import com.ems.ems_app.entities.AcademicTerm;
import com.ems.ems_app.entities.AcademicYear;
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
public class AcademicTermRepository {
    @Autowired
    private DataSource dataSource;


        public AcademicTerm save(AcademicTerm term, Connection conn) throws SQLException {
            String sql = "INSERT INTO academic_terms (id, academic_year_id, name, term_order,start_date, end_date, created_at, updated_at) VALUES (?,?, ?, ?, ?, ?, ?, ?)";
           try (PreparedStatement ps = conn.prepareStatement(sql)) {
            UUID id = UUID.randomUUID();
            term.setId(id);
            LocalDateTime now = LocalDateTime.now();
            Timestamp nowTimestamp = Timestamp.valueOf(now);

            ps.setObject(1, id);
            ps.setObject(2, term.getAcademicYear().getId()); // Get ID from nested AcademicYear entity
            ps.setString(3, term.getName());
               ps.setInt(4, term.getTermOrder());
               ps.setDate(5, Date.valueOf(term.getStartDate()));
            ps.setDate(6, Date.valueOf(term.getEndDate()));
            ps.setTimestamp(7, nowTimestamp);
            ps.setTimestamp(8, nowTimestamp);

            ps.executeUpdate();
            term.setCreatedAt(now);
            term.setUpdatedAt(now);
            return term;
        }
    }
    public List<AcademicTerm> saveAll(List<AcademicTerm> terms, Connection conn) throws SQLException {
        // Consider using batch updates for better performance if saving many terms
        List<AcademicTerm> savedTerms = new ArrayList<>();
        for (AcademicTerm term : terms) {
            savedTerms.add(save(term, conn));
        }
        return savedTerms;
    }
    public Optional<AcademicTerm> findById(UUID id) throws SQLException {
        String sql = "SELECT * FROM academic_terms WHERE id = ?";
        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    public List<AcademicTerm> findAll() throws SQLException {
        List<AcademicTerm> terms = new ArrayList<>();
        String sql = "SELECT * FROM academic_terms";
        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                terms.add(mapRow(rs));
            }
        }
        return terms;
    }

    private AcademicTerm mapRow(ResultSet rs) throws SQLException {
        AcademicYear year = new AcademicYear();
        year.setId((UUID) rs.getObject("academic_year_id"));
        return AcademicTerm.builder()
                .id((UUID) rs.getObject("id"))
                .academicYear(year)
                .name(rs.getString("name"))
                .termOrder(rs.getInt("term_order"))
                .startDate(rs.getDate("start_date").toLocalDate())
                .endDate(rs.getDate("end_date").toLocalDate())
                .status(rs.getString("status"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                .build();
    }
    public void deleteById(UUID id) {
        String sql = "DELETE FROM academic_terms WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting academic term", e);
        }
    }
}


