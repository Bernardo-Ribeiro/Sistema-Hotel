package com.hotel.gerenciador.dao;

import com.hotel.gerenciador.model.Reserva;
import com.hotel.gerenciador.model.Hospede;
import com.hotel.gerenciador.model.Quarto;
import com.hotel.gerenciador.util.StatusReserva;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class ReservaDAO extends BaseDAO<Reserva> {

    @Override
    protected String getTableName() {
        return "Reservas";
    }

    @Override
    protected Reserva fromResultSet(ResultSet rs) throws SQLException {
        HospedeDAO hospedeDAO = new HospedeDAO();
        QuartoDAO quartoDAO = new QuartoDAO();

        int hospedeId = rs.getInt("HospedeID");
        int quartoId = rs.getInt("QuartoID");

        Hospede hospede = hospedeDAO.findById(hospedeId);
        Quarto quarto = quartoDAO.findById(quartoId);

        return new Reserva(
            rs.getInt("ReservaID"),
            hospede,
            quarto,
            rs.getDate("DataCheckIn").toLocalDate(),
            rs.getDate("DataCheckOut").toLocalDate(),
            StatusReserva.valueOf(rs.getString("Status")),
            rs.getTimestamp("DataCriacao").toLocalDateTime(),
            rs.getTimestamp("DataAtualizacao").toLocalDateTime()
        );
    }

    public boolean insert(Reserva reserva) throws SQLException {
        String sql = "INSERT INTO Reservas (HospedeID, QuartoID, DataCheckIn, DataCheckOut, Status, ValorTotal) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, reserva.getHospede().getId());
            stmt.setInt(2, reserva.getQuarto().getId());
            stmt.setDate(3, Date.valueOf(reserva.getDataCheckIn()));
            stmt.setDate(4, Date.valueOf(reserva.getDataCheckOut()));
            stmt.setString(5, reserva.getStatus().name());
            stmt.setDouble(6, reserva.getValorTotal());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        reserva.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean update(Reserva reserva) throws SQLException {
        String sql = "UPDATE Reservas SET HospedeID = ?, QuartoID = ?, DataCheckIn = ?, DataCheckOut = ?, Status = ?, ValorTotal = ? " +
                     "WHERE ReservaID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reserva.getHospede().getId());
            stmt.setInt(2, reserva.getQuarto().getId());
            stmt.setDate(3, Date.valueOf(reserva.getDataCheckIn()));
            stmt.setDate(4, Date.valueOf(reserva.getDataCheckOut()));
            stmt.setString(5, reserva.getStatus().name());
            stmt.setDouble(6, reserva.getValorTotal());
            stmt.setInt(7, reserva.getId());

            return stmt.executeUpdate() > 0;
        }
    }
    public List<Reserva> findByStatus(StatusReserva status) throws SQLException {
        String sql = "SELECT * FROM Reservas WHERE Status = ?";
    
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setString(1, status.name());
    
            try (ResultSet rs = stmt.executeQuery()) {
                List<Reserva> reservas = new ArrayList<>();
                while (rs.next()) {
                    reservas.add(fromResultSet(rs));
                }
                return reservas;
            }
        }
    }
    
}
