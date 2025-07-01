package com.hotel.gerenciador.daos;

import com.hotel.gerenciador.models.Consumo;

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
    protected String getIdColumnName() {
        return "ConsumoID";
    }

    @Override
    protected Consumo fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("ConsumoID");
        int idReserva = rs.getInt("ReservaID");
        int idProduto = rs.getInt("ProdutoID");
        int quantidade = rs.getInt("Quantidade");
        double valor = rs.getDouble("Valor");
        LocalDate dataConsumo = rs.getDate("DataConsumo").toLocalDate();
        LocalDateTime dataCriacao = rs.getTimestamp("DataCriacao").toLocalDateTime();
        LocalDateTime dataAtualizacao = rs.getTimestamp("DataAtualizacao").toLocalDateTime();

        return new Consumo(id, idReserva, idProduto, valor, quantidade, dataConsumo, dataCriacao, dataAtualizacao);
    }

    public boolean insert(Consumo consumo) throws SQLException {
        String sql = "INSERT INTO Consumo (ReservaID, ProdutoID, Quantidade, Valor, DataConsumo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, consumo.getIdReserva());
            stmt.setInt(2, consumo.getIdProduto());
            stmt.setInt(3, consumo.getQuantidade());
            stmt.setDouble(4, consumo.getValor());
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
        String sql = "UPDATE Consumo SET ReservaID = ?, ProdutoID = ?, Quantidade = ?, Valor = ?, DataConsumo = ? WHERE ConsumoID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, consumo.getIdReserva());
            stmt.setInt(2, consumo.getIdProduto());
            stmt.setInt(3, consumo.getQuantidade());
            stmt.setDouble(4, consumo.getValor());
            stmt.setDate(5, Date.valueOf(consumo.getDataConsumo()));
            stmt.setInt(6, consumo.getId());

            return stmt.executeUpdate() > 0;
        }
    }
    public List<Consumo> findByReservaId(int reservaId) throws SQLException {
        String sql = "SELECT * FROM Consumo WHERE ReservaID = ?";
        List<Consumo> lista = new ArrayList<>();

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
