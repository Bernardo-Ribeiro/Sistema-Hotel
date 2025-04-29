package com.hotel.gerenciador.dao;

import com.hotel.gerenciador.model.Quarto;
import com.hotel.gerenciador.util.StatusQuarto;
import com.hotel.gerenciador.util.TipoQuarto;

import java.sql.*;
import java.time.LocalDateTime;

public class QuartoDAO extends BaseDAO<Quarto> {

    @Override
    protected String getTableName() {
        return "Quartos";
    }

    @Override
    protected Quarto fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("QuartoID");
        int numeroQuarto = rs.getInt("NumeroQuarto");
        TipoQuarto tipo = TipoQuarto.valueOf(rs.getString("Tipo"));
        double precoDiaria = rs.getDouble("PrecoDiaria");
        int capacidade = rs.getInt("Capacidade");
        StatusQuarto status = StatusQuarto.valueOf(rs.getString("Status"));
        LocalDateTime dataCriacao = rs.getTimestamp("DataCriacao").toLocalDateTime();
        LocalDateTime dataAtualizacao = rs.getTimestamp("DataAtualizacao").toLocalDateTime();

        return new Quarto(id, numeroQuarto, tipo, precoDiaria, capacidade, status, dataCriacao, dataAtualizacao);
    }

    public boolean insert(Quarto quarto) throws SQLException {
        String sql = "INSERT INTO Quartos (NumeroQuarto, Tipo, PrecoDiaria, Capacidade, Status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, quarto.getNumeroQuarto());
            stmt.setString(2, quarto.getTipo().toString());
            stmt.setDouble(3, quarto.getPrecoDiaria());
            stmt.setInt(4, quarto.getCapacidade());
            stmt.setString(5, quarto.getStatus().toString());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        quarto.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean update(Quarto quarto) throws SQLException {
        String sql = "UPDATE Quartos SET NumeroQuarto = ?, Tipo = ?, PrecoDiaria = ?, Capacidade = ?, Status = ? WHERE QuartoID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, quarto.getNumeroQuarto());
            stmt.setString(2, quarto.getTipo().toString());
            stmt.setDouble(3, quarto.getPrecoDiaria());
            stmt.setInt(4, quarto.getCapacidade());
            stmt.setString(5, quarto.getStatus().toString());
            stmt.setInt(6, quarto.getId());

            return stmt.executeUpdate() > 0;
        }
    }
}
