package com.ems.ems_app.repos;

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
public class AcademicYearRepository {
    @Autowired
    private DataSource dataSource;


public AcademicYear save(AcademicYear year, Connection conn) throws SQLException {
    String sql = "INSERT INTO academic_years (id, year, start_date, end_date, status, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
    // Use the passed-in connection, do not close it here
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        UUID id = UUID.randomUUID();
        year.setId(id);
        // Use LocalDateTime directly
        LocalDateTime now = LocalDateTime.now();
        Timestamp nowTimestamp = Timestamp.valueOf(now);

        ps.setObject(1, id);
        ps.setInt(2, year.getYear());
        ps.setDate(3, Date.valueOf(year.getStartDate()));
        ps.setDate(4, Date.valueOf(year.getEndDate()));
        ps.setString(5, year.getStatus() != null ? year.getStatus() : "PLANNED"); // Provide a default status if null
        ps.setTimestamp(6, nowTimestamp);
        ps.setTimestamp(7, nowTimestamp);

        ps.executeUpdate();
        // Set timestamps on the entity object
        year.setCreatedAt(now);
        year.setUpdatedAt(now);
        return year;
    }
}

    public Optional<AcademicYear> findById(UUID id) throws SQLException {
        String sql = "SELECT * FROM academic_years WHERE id = ?";
        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    public List<AcademicYear> findAll() throws SQLException {
        List<AcademicYear> years = new ArrayList<>();
        String sql = "SELECT * FROM academic_years";
        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                years.add(mapRow(rs));
            }
        }
        return years;
    }

    private AcademicYear mapRow(ResultSet rs) throws SQLException {
        return AcademicYear.builder()
                .id((UUID) rs.getObject("id"))
                .year(rs.getInt("year"))
                .startDate(rs.getDate("start_date").toLocalDate())
                .endDate(rs.getDate("end_date").toLocalDate())
                .status(rs.getString("status"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                .build();
    }
        public void deleteById(UUID id) {
        String sql = "DELETE FROM academic_years WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting academic year", e);
        }
    }
    public void update(AcademicYear academicYear) {
        String sql = "UPDATE academic_years SET year = ?, start_date = ?, end_date = ?, status = ?, updated_at = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, academicYear.getYear());
            statement.setDate(2, Date.valueOf(academicYear.getStartDate()));
            statement.setDate(3, Date.valueOf(academicYear.getEndDate()));
            statement.setString(4, academicYear.getStatus());
            statement.setTimestamp(5, Timestamp.valueOf(academicYear.getUpdatedAt()));
            statement.setObject(6, academicYear.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating academic year", e);
        }
    }


}




 //   public void deleteById(UUID id) {
//        String sql = "DELETE FROM academic_years WHERE id = ?";
//
//        try (Connection connection = dataSource.getConnection();
//             PreparedStatement statement = connection.prepareStatement(sql)) {
//
//            statement.setObject(1, id);
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException("Error deleting academic year", e);
//        }
//    }
//
//    private AcademicYear mapResultSetToAcademicYear(ResultSet resultSet) throws SQLException {
//        return new AcademicYear(
//                UUID.fromString(resultSet.getString("id")),
//                resultSet.getInt("year"),
//                resultSet.getDate("start_date").toLocalDate(),
//                resultSet.getDate("end_date").toLocalDate(),
//                resultSet.getString("status"),
//                resultSet.getTimestamp("created_at").toLocalDateTime(),
//                resultSet.getTimestamp("updated_at").toLocalDateTime()
//        );
//    }
//}
