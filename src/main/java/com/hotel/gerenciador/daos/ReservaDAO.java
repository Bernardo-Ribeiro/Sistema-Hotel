package com.hotel.gerenciador.daos;

import com.hotel.gerenciador.models.Hospede;
import com.hotel.gerenciador.models.Quarto;
import com.hotel.gerenciador.models.Reserva;
import com.hotel.gerenciador.utils.StatusReserva;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO extends BaseDAO<Reserva> {

    @Override
    protected String getTableName() {
        return "reservas";
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
        BigDecimal valorTotal = rs.getBigDecimal("ValorTotal");
        
        LocalDateTime dataCriacao = null;
        Timestamp dataCriacaoTs = rs.getTimestamp("DataCriacao");
        if (dataCriacaoTs != null) {
            dataCriacao = dataCriacaoTs.toLocalDateTime();
        }

        LocalDateTime dataAtualizacao = null;
        Timestamp dataAtualizacaoTs = rs.getTimestamp("DataAtualizacao");
        if (dataAtualizacaoTs != null) {
            dataAtualizacao = dataAtualizacaoTs.toLocalDateTime();
        }

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
        String sql = "INSERT INTO reservas (HospedeID, QuartoID, DataCheckIn, DataCheckOut, Status, ValorTotal) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (reserva.getHospede() == null || reserva.getQuarto() == null) {
                throw new SQLException("Hóspede e Quarto não podem ser nulos para inserir uma reserva.");
            }
            stmt.setInt(1, reserva.getHospede().getId());
            stmt.setInt(2, reserva.getQuarto().getId());
            stmt.setDate(3, Date.valueOf(reserva.getDataCheckIn()));
            stmt.setDate(4, Date.valueOf(reserva.getDataCheckOut()));
            stmt.setString(5, reserva.getStatus().name());
            stmt.setBigDecimal(6, reserva.getValorTotal());

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
        String sql = "UPDATE reservas SET HospedeID = ?, QuartoID = ?, DataCheckIn = ?, DataCheckOut = ?, Status = ?, ValorTotal = ? " +
                     "WHERE ReservaID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (reserva.getHospede() == null || reserva.getQuarto() == null) {
                throw new SQLException("Hóspede e Quarto não podem ser nulos para atualizar uma reserva.");
            }
            stmt.setInt(1, reserva.getHospede().getId());
            stmt.setInt(2, reserva.getQuarto().getId());
            stmt.setDate(3, Date.valueOf(reserva.getDataCheckIn()));
            stmt.setDate(4, Date.valueOf(reserva.getDataCheckOut()));
            stmt.setString(5, reserva.getStatus().name());
            stmt.setBigDecimal(6, reserva.getValorTotal());
            stmt.setInt(7, reserva.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    public List<Reserva> findByStatus(StatusReserva status) throws SQLException {
        String sql = "SELECT * FROM reservas WHERE Status = ?";
    
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
        String sql = "SELECT * FROM reservas WHERE QuartoID = ? AND " +
                     "Status NOT IN ('CANCELADA') AND NOT Status = 'PENDENTE' AND (" +
                     "  (DataCheckIn <= ? AND DataCheckOut >= ?) OR " +
                     "  (DataCheckIn BETWEEN ? AND ?) OR " +
                     "  (DataCheckOut BETWEEN ? AND ?)" +
                     ")";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, quartoId);
            stmt.setDate(2, Date.valueOf(checkOut));
            stmt.setDate(3, Date.valueOf(checkIn));
            stmt.setDate(4, Date.valueOf(checkIn));
            stmt.setDate(5, Date.valueOf(checkOut.minusDays(1)));
            stmt.setDate(6, Date.valueOf(checkIn.plusDays(1)));
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
        String sql = "SELECT * FROM reservas WHERE HospedeID = ? ORDER BY DataCheckIn DESC";
        
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
        String sql = "SELECT * FROM reservas WHERE QuartoID = ? AND Status NOT IN ('CANCELADA', 'HOSPEDADO', 'CONCLUIDA')";
        
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
        String sql = "SELECT r.* FROM reservas r " +
                    "JOIN quartos q ON r.QuartoID = q.QuartoID " +
                    "WHERE q.Tipo = ? AND r.Status NOT IN ('CANCELADA', 'HOSPEDADO', 'CONCLUIDA')"; 
        
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
        String sql = "SELECT * FROM reservas WHERE Status NOT IN ('CANCELADA', 'CONCLUIDA')";
        
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

    public List<Reserva> buscarComFiltros(StatusReserva status, LocalDate dataDe, LocalDate dataAte, String termoBusca) throws SQLException {
        List<Reserva> listaReservas = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder(
            "SELECT DISTINCT r.* FROM reservas r " +
            "LEFT JOIN hospedes h ON r.HospedeID = h.HospedeID " +
            "LEFT JOIN quartos q ON r.QuartoID = q.QuartoID " +
            "WHERE 1=1"
        );

        if (status != null) {
            sqlBuilder.append(" AND r.Status = ?");
            params.add(status.name());
        }

        if (dataDe != null && dataAte != null) {
            sqlBuilder.append(" AND r.DataCheckIn <= ? AND r.DataCheckOut >= ?");
            params.add(Date.valueOf(dataAte));
            params.add(Date.valueOf(dataDe));
        } else if (dataDe != null) {
            sqlBuilder.append(" AND r.DataCheckOut >= ?");
            params.add(Date.valueOf(dataDe));
        } else if (dataAte != null) {
            sqlBuilder.append(" AND r.DataCheckIn <= ?");
            params.add(Date.valueOf(dataAte));
        }

        if (termoBusca != null && !termoBusca.trim().isEmpty()) {
            String likeTermo = "%" + termoBusca.trim().toLowerCase() + "%";
            sqlBuilder.append(" AND (LOWER(h.Nome) LIKE ? OR CAST(q.NumeroQuarto AS CHAR) LIKE ? OR CAST(r.ReservaID AS CHAR) LIKE ?)");
            params.add(likeTermo);
            params.add(likeTermo);
            params.add(likeTermo);
        }

        sqlBuilder.append(" ORDER BY r.DataCheckIn DESC, r.ReservaID DESC");

        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {

            int paramIndex = 1;
            for (Object param : params) {
                stmt.setObject(paramIndex++, param);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    listaReservas.add(fromResultSet(rs));
                }
            }
        }
        return listaReservas;
    }
    public List<Reserva> findByQuartoAndPeriodo(int quartoId, LocalDate checkIn, LocalDate checkOut, int reservaIdParaIgnorar) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder(
            "SELECT * FROM reservas WHERE QuartoID = ? " +
            "AND Status NOT IN ('CANCELADA') " + 
            "AND DataCheckIn < ? AND DataCheckOut > ? " 
        );
        List<Object> params = new ArrayList<>();
        params.add(quartoId);
        params.add(Date.valueOf(checkOut));
        params.add(Date.valueOf(checkIn));

        if (reservaIdParaIgnorar > 0) {
            sqlBuilder.append(" AND ReservaID != ?");
            params.add(reservaIdParaIgnorar);
        }

        List<Reserva> lista = new ArrayList<>();
        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {
            int paramIndex = 1;
            for (Object param : params) {
                stmt.setObject(paramIndex++, param);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(fromResultSet(rs));
            }
        }
        return lista;
    }
}