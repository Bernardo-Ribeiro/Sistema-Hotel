package com.hotel.gerenciador.daos;

import com.hotel.gerenciador.models.ConsumoServicos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsumoServicosDAO extends BaseDAO<ConsumoServicos> {

    @Override
    protected String getTableName() {
        return "consumoservicos";
    }
    protected String getIdColumnName() {
        return "ConsumoServicoID";
    }

    @Override
    protected ConsumoServicos fromResultSet(ResultSet rs) throws SQLException {
        return new ConsumoServicos(
                rs.getInt("ConsumoServicoID"),
                rs.getInt("ReservaID"),
                rs.getInt("ServicoID"),
                rs.getInt("Quantidade"),
                rs.getDate("DataConsumo").toLocalDate(),
                rs.getTimestamp("DataCriacao").toLocalDateTime(),
                rs.getTimestamp("DataAtualizacao").toLocalDateTime()
        );
    }

    public void insert(ConsumoServicos consumo) throws SQLException {
        String sql = "INSERT INTO consumoservicos (ReservaID, ServicoID, Quantidade, DataConsumo) VALUES (?, ?, ?, ?)";
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
        String sql = "UPDATE consumoservicos SET ReservaID=?, ServicoID=?, Quantidade=?, DataConsumo=? WHERE ConsumoServicoID=?";
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

    public List<ConsumoServicos> findByReservaId(int reservaId) throws SQLException {
        List<ConsumoServicos> lista = new ArrayList<>();
        String sql = "SELECT * FROM consumoservicos WHERE ReservaID = ?";

        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(fromResultSet(rs));
                }
            }
        }
        return lista;
    }
}