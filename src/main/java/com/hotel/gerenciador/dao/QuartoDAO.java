package com.hotel.gerenciador.dao;

import com.hotel.gerenciador.model.Quarto;
import com.hotel.gerenciador.util.StatusQuarto;
import com.hotel.gerenciador.util.TipoQuarto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QuartoDAO extends BaseDAO<Quarto> {

    @Override
    protected String getTableName() {
        return "Quartos";
    }

    @Override
    protected String getIdColumnName() {
        return "QuartoID";
    }

    @Override
    protected Quarto fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("QuartoID");
        int numeroQuarto = rs.getInt("NumeroQuarto");
        TipoQuarto tipo = TipoQuarto.valueOf(rs.getString("Tipo"));
        BigDecimal precoDiaria = rs.getBigDecimal("PrecoDiaria");
        StatusQuarto status = StatusQuarto.valueOf(rs.getString("Status"));
        
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

        return new Quarto(id, numeroQuarto, tipo, precoDiaria, status, dataCriacao, dataAtualizacao);
    }

    public boolean insert(Quarto quarto) throws SQLException {
        String sql = "INSERT INTO Quartos (NumeroQuarto, Tipo, PrecoDiaria, Status) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, quarto.getNumeroQuarto());
            stmt.setString(2, quarto.getTipo().name());
            stmt.setBigDecimal(3, quarto.getPrecoDiaria());
            stmt.setString(4, quarto.getStatus().name());

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
        String sql = "UPDATE Quartos SET NumeroQuarto = ?, Tipo = ?, PrecoDiaria = ?, Status = ? WHERE QuartoID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, quarto.getNumeroQuarto());
            stmt.setString(2, quarto.getTipo().name());
            stmt.setBigDecimal(3, quarto.getPrecoDiaria());
            stmt.setString(4, quarto.getStatus().name());
            stmt.setInt(5, quarto.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    public List<Quarto> findByTipo(TipoQuarto tipo) throws SQLException {
        List<Quarto> quartosDoTipo = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName() + " WHERE Tipo = ?";
        
        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tipo.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    quartosDoTipo.add(fromResultSet(rs));
                }
            }
        }
        return quartosDoTipo;
    }
    public Quarto findByNumero(int numeroQuarto) throws SQLException {
        String sql = "SELECT * FROM " + getTableName() + " WHERE NumeroQuarto = ?";
        Quarto quarto = null;

        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, numeroQuarto);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                quarto = fromResultSet(rs);
            }
        }
        return quarto;
    }
}