package com.hotel.gerenciador.dao;

import com.hotel.gerenciador.model.Consumo;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConsumoDAO extends BaseDAO<Consumo> {

    @Override
    protected String getTableName() {
        return "Consumo";
    }

    @Override
    protected Consumo fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("ConsumoID");
        int idHospede = rs.getInt("HospedeID");
        int idProduto = rs.getInt("ProdutoID");
        double valor = rs.getDouble("Valor");
        int quantidade = rs.getInt("Quantidade");
        LocalDate dataConsumo = rs.getDate("DataConsumo").toLocalDate();
        LocalDateTime dataCriacao = rs.getTimestamp("DataCriacao").toLocalDateTime();
        LocalDateTime dataAtualizacao = rs.getTimestamp("DataAtualizacao").toLocalDateTime();

        return new Consumo(id, idHospede, idProduto, valor, quantidade, dataConsumo, dataCriacao, dataAtualizacao);
    }

    public boolean insert(Consumo consumo) throws SQLException {
        String sql = "INSERT INTO Consumo (HospedeID, ProdutoID, Valor, Quantidade, DataConsumo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, consumo.getIdHospede());
            stmt.setInt(2, consumo.getIdProduto());
            stmt.setDouble(3, consumo.getValor());
            stmt.setInt(4, consumo.getQuantidade());
            stmt.setDate(5, Date.valueOf(consumo.getDataConsumo()));

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        consumo.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean update(Consumo consumo) throws SQLException {
        String sql = "UPDATE Consumo SET HospedeID = ?, ProdutoID = ?, Valor = ?, Quantidade = ?, DataConsumo = ? WHERE ConsumoID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, consumo.getIdHospede());
            stmt.setInt(2, consumo.getIdProduto());
            stmt.setDouble(3, consumo.getValor());
            stmt.setInt(4, consumo.getQuantidade());
            stmt.setDate(5, Date.valueOf(consumo.getDataConsumo()));
            stmt.setInt(6, consumo.getId());

            return stmt.executeUpdate() > 0;
        }
    }
}