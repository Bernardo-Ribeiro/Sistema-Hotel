package com.hotel.gerenciador.dao;

import com.hotel.gerenciador.model.Hospede;
import com.hotel.gerenciador.model.Quarto;
import com.hotel.gerenciador.model.Reserva;
import com.hotel.gerenciador.util.StatusReserva;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReservaDAO extends BaseDAO<Reserva> {

    @Override
    protected String getTableName() {
        return "Reservas";
    }
    @Override
    protected String getIdColumnName() {
        return "ReservaID";
    }

    @Override
    protected Reserva fromResultSet(ResultSet rs) throws SQLException {
        int reservaId = rs.getInt("ReservaID");
        int hospedeId = rs.getInt("HospedeID");
        int quartoId = rs.getInt("QuartoID");
        LocalDate dataCheckIn = rs.getDate("DataCheckIn").toLocalDate();
        LocalDate dataCheckOut = rs.getDate("DataCheckOut").toLocalDate();
        StatusReserva status = StatusReserva.valueOf(rs.getString("Status"));
        double valorTotal = rs.getDouble("ValorTotal");
        LocalDateTime dataCriacao = rs.getTimestamp("DataCriacao").toLocalDateTime();
        LocalDateTime dataAtualizacao = rs.getTimestamp("DataAtualizacao").toLocalDateTime();

        Hospede hospede = null;
        try {
            hospede = new HospedeDAO().findById(hospedeId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Quarto quarto = null;
        try {
            quarto = new QuartoDAO().findById(quartoId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Reserva(
            reservaId,
            hospede,
            quarto,
            dataCheckIn,
            dataCheckOut,
            status,
            valorTotal,
            dataCriacao,
            dataAtualizacao
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

    public List<Reserva> findByQuartoAndPeriodo(int quartoId, LocalDate checkIn, LocalDate checkOut) throws SQLException {
        String sql = "SELECT * FROM Reservas WHERE QuartoID = ? AND " +
                     "((DataCheckIn BETWEEN ? AND ?) OR " +
                     "(DataCheckOut BETWEEN ? AND ?) OR " +
                     "(DataCheckIn <= ? AND DataCheckOut >= ?)) AND " +
                     "Status NOT IN ('CANCELADA', 'PENDENTE')";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, quartoId);
            stmt.setDate(2, Date.valueOf(checkIn));
            stmt.setDate(3, Date.valueOf(checkOut.minusDays(1)));
            stmt.setDate(4, Date.valueOf(checkIn.plusDays(1)));
            stmt.setDate(5, Date.valueOf(checkOut));
            stmt.setDate(6, Date.valueOf(checkIn));
            stmt.setDate(7, Date.valueOf(checkOut));
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<Reserva> reservas = new ArrayList<>();
                while (rs.next()) {
                    reservas.add(fromResultSet(rs));
                }
                return reservas;
            }
        }
    }

    public List<Reserva> findByHospede(int hospedeId) throws SQLException {
        String sql = "SELECT * FROM Reservas WHERE HospedeID = ? ORDER BY DataCheckIn DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, hospedeId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<Reserva> reservas = new ArrayList<>();
                while (rs.next()) {
                    reservas.add(fromResultSet(rs));
                }
                return reservas;
            }
        }
    }

    public List<Reserva> findAtivasByQuarto(int quartoId) throws SQLException {
        String sql = "SELECT * FROM Reservas WHERE QuartoID = ? AND Status NOT IN ('CANCELADA', 'PENDENTE', 'CONCLUIDA')";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, quartoId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<Reserva> reservas = new ArrayList<>();
                while (rs.next()) {
                    reservas.add(fromResultSet(rs));
                }
                return reservas;
            }
        }
    }
    public List<Reserva> findByTipo(String tipoQuarto) throws SQLException {
        String sql = "SELECT r.* FROM Reservas r " +
                    "JOIN Quartos q ON r.QuartoID = q.QuartoID " +
                    "WHERE q.Tipo = ? AND r.Status NOT IN ('CANCELADA', 'PENDENTE')";
        
        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tipoQuarto);
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<Reserva> reservas = new ArrayList<>();
                while (rs.next()) {
                    reservas.add(fromResultSet(rs));
                }
                return reservas;
            }
        }
    }
    public List<Reserva> findAtivas() throws SQLException {
        String sql = "SELECT * FROM Reservas WHERE Status NOT IN ('CANCELADA', 'CONCLUIDA') ";
        
        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            List<Reserva> reservas = new ArrayList<>();
            while (rs.next()) {
                reservas.add(fromResultSet(rs));
            }
            return reservas;
        }
    }
}