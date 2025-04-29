package com.hotel.gerenciador.dao;

import com.hotel.gerenciador.model.Manutencao;
import com.hotel.gerenciador.util.StatusManutencao;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ManutencaoDAO extends BaseDAO<Manutencao> {

    @Override
    protected String getTableName() {
        return "Manutencao";
    }

    @Override
    protected Manutencao fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("ManutencaoID");
        int idQuarto = rs.getInt("QuartoID");
        LocalDate dataInicio = rs.getDate("DataInicio").toLocalDate();
        LocalDate dataFim = rs.getDate("DataFim") != null ? rs.getDate("DataFim").toLocalDate() : null;
        String descricao = rs.getString("Descricao");
        StatusManutencao status = StatusManutencao.valueOf(rs.getString("Status"));
        int idFuncionario = rs.getInt("FuncionarioID");
        LocalDateTime dataCriacao = rs.getTimestamp("DataCriacao").toLocalDateTime();
        LocalDateTime dataAtualizacao = rs.getTimestamp("DataAtualizacao").toLocalDateTime();

        return new Manutencao(idQuarto, dataInicio, dataFim, descricao, status, idFuncionario);
    }

    public boolean insert(Manutencao manutencao) throws SQLException {
        String sql = "INSERT INTO Manutencao (QuartoID, DataInicio, DataFim, Descricao, Status, FuncionarioID) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, manutencao.getIdQuarto());
            stmt.setDate(2, Date.valueOf(manutencao.getDataInicio()));
            stmt.setDate(3, manutencao.getDataFim() != null ? Date.valueOf(manutencao.getDataFim()) : null);
            stmt.setString(4, manutencao.getDescricao());
            stmt.setString(5, manutencao.getStatus().name());
            stmt.setInt(6, manutencao.getIdFuncionario());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        manutencao.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean update(Manutencao manutencao) throws SQLException {
        String sql = "UPDATE Manutencao SET QuartoID = ?, DataInicio = ?, DataFim = ?, Descricao = ?, " +
                     "Status = ?, FuncionarioID = ? WHERE ManutencaoID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, manutencao.getIdQuarto());
            stmt.setDate(2, Date.valueOf(manutencao.getDataInicio()));
            stmt.setDate(3, manutencao.getDataFim() != null ? Date.valueOf(manutencao.getDataFim()) : null);
            stmt.setString(4, manutencao.getDescricao());
            stmt.setString(5, manutencao.getStatus().name());
            stmt.setInt(6, manutencao.getIdFuncionario());
            stmt.setInt(7, manutencao.getId());

            return stmt.executeUpdate() > 0;
        }
    }
}
