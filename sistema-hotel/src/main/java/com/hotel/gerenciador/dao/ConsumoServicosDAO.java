package com.hotel.gerenciador.dao;

import com.hotel.gerenciador.model.ConsumoServicos;

import java.sql.*;

public class ConsumoServicosDAO extends BaseDAO<ConsumoServicos> {

    @Override
    protected String getTableName() {
        return "ConsumoServicos";
    }

    @Override
    protected ConsumoServicos fromResultSet(ResultSet rs) throws SQLException {
        return new ConsumoServicos(
                rs.getInt("ConsumoID"),
                rs.getInt("ReservaID"),
                rs.getInt("ServicoID"),
                rs.getInt("Quantidade"),
                rs.getDate("DataConsumo").toLocalDate(),
                rs.getTimestamp("DataCriacao").toLocalDateTime(),
                rs.getTimestamp("DataAtualizacao").toLocalDateTime()
        );
    }

    public void insert(ConsumoServicos consumo) throws SQLException {
        String sql = "INSERT INTO ConsumoServicos (ReservaID, ServicoID, Quantidade, DataConsumo) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, consumo.getReservaId());
            stmt.setInt(2, consumo.getServicoId());
            stmt.setInt(3, consumo.getQuantidade());
            stmt.setDate(4, Date.valueOf(consumo.getDataConsumo()));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                consumo.setId(rs.getInt(1));
            }
        }
    }

    public boolean update(ConsumoServicos consumo) throws SQLException {
        String sql = "UPDATE ConsumoServicos SET ReservaID=?, ServicoID=?, Quantidade=?, DataConsumo=? WHERE ConsumoID=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, consumo.getReservaId());
            stmt.setInt(2, consumo.getServicoId());
            stmt.setInt(3, consumo.getQuantidade());
            stmt.setDate(5, Date.valueOf(consumo.getDataConsumo()));
            stmt.setInt(6, consumo.getId());

            return stmt.executeUpdate() > 0;
        }
    }
}
