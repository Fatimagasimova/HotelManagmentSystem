package az.fatia.repository;

import az.fatia.config.DatabaseConfig;
import az.fatia.enums.Status;
import az.fatia.model.Apartment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcApartmentRepository implements ApartmentRepository {

    @Override
    public void save(Apartment apartment) {
        String query = "INSERT INTO apartments (id, price, status, client_name) VALUES (?, ?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET price = EXCLUDED.price, status = EXCLUDED.status, client_name = EXCLUDED.client_name";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, apartment.getId());
            ps.setBigDecimal(2, apartment.getPrice());
            ps.setString(3, apartment.getStatus().name());
            ps.setString(4, apartment.getClientName());

            ps.executeUpdate();

            if (!conn.getAutoCommit()) {
                conn.commit();
            }

        } catch (SQLException e) {
            System.err.println("!!! REPOSITORY: ERROR IN SAVE METHOD !!!");
            e.printStackTrace();
            throw new RuntimeException("Database error during save: " + e.getMessage(), e);
        }
    }

    @Override
    public Apartment findById(int id) {
        String query = "SELECT * FROM apartments WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Apartment apt = new Apartment();
                    apt.setId(rs.getInt("id"));
                    apt.setPrice(rs.getBigDecimal("price"));
                    apt.setStatus(Status.valueOf(rs.getString("status")));
                    apt.setClientName(rs.getString("client_name"));
                    return apt;
                }
            }
        } catch (SQLException e) {
            System.err.println("!!! REPOSITORY: ERROR IN FINDBYID METHOD !!!");
            e.printStackTrace();
            throw new RuntimeException("Database error during findById: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Apartment> findAll() {
        List<Apartment> list = new ArrayList<>();
        String query = "SELECT * FROM apartments";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Apartment apt = new Apartment();
                apt.setId(rs.getInt("id"));
                apt.setPrice(rs.getBigDecimal("price"));
                apt.setStatus(Status.valueOf(rs.getString("status")));
                apt.setClientName(rs.getString("client_name"));
                list.add(apt);
            }
        } catch (SQLException e) {
            System.err.println("!!! REPOSITORY: ERROR IN FINDALL METHOD !!!");
            e.printStackTrace();
            throw new RuntimeException("Database error during findAll: " + e.getMessage(), e);
        }
        return list;
    }
}