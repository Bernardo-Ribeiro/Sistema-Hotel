package com.hotel.gerenciador.dao;

import com.hotel.gerenciador.model.Consumo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsumoDAO {

    public void adicionarConsumo(Consumo consumo) {
        String sql = "INSERT INTO Consumo (idHospede, idProduto, valor, quantidade, dataConsumo, DataCriacao, DataAtualizacao) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, consumo.getIdHospede());
            stmt.setInt(2, consumo.getIdProduto());
            stmt.setDouble(3, consumo.getValor());
            stmt.setInt(4, consumo.getQuantidade());
            stmt.setDate(5, Date.valueOf(consumo.getDataConsumo()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Consumo> listarConsumos() {
        List<Consumo> consumos = new ArrayList<>();
        String sql = "SELECT * FROM Consumo";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Consumo consumo = new Consumo(
                        rs.getInt("id"),
                        rs.getInt("idHospede"),
                        rs.getInt("idProduto"),
                        rs.getDouble("valor"),
                        rs.getInt("quantidade"),
                        rs.getDate("dataConsumo").toLocalDate(),
                        rs.getTimestamp("DataCriacao").toLocalDateTime(),
                        rs.getTimestamp("DataAtualizacao").toLocalDateTime()
                );
                consumos.add(consumo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return consumos;
    }

    public boolean atualizarConsumo(Consumo consumo) {
        String sql = "UPDATE Consumo SET idHospede = ?, idProduto = ?, valor = ?, quantidade = ?, dataConsumo = ? WHERE id = ?";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setInt(1, consumo.getIdHospede());
            stmt.setInt(2, consumo.getIdProduto());
            stmt.setDouble(3, consumo.getValor());
            stmt.setInt(4, consumo.getQuantidade());
            stmt.setDate(5, Date.valueOf(consumo.getDataConsumo()));
            stmt.setInt(6, consumo.getId());
    
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
    
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deletarConsumo(int id) {
        String sql = "DELETE FROM Consumo WHERE id = ?";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
    
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }    
}
