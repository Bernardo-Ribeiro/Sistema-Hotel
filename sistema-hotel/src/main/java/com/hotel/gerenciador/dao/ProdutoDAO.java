package com.hotel.gerenciador.dao;

import com.hotel.gerenciador.model.Produto;
import com.hotel.gerenciador.util.CategoriaProduto;

import java.sql.*;
import java.time.LocalDateTime;

public class ProdutoDAO extends BaseDAO<Produto> {

    @Override
    protected String getTableName() {
        return "Produtos";
    }

    @Override
    protected Produto fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("ProdutoID");
        String nome = rs.getString("Nome");
        String descricao = rs.getString("Descricao");
        double preco = rs.getDouble("Preco");
        CategoriaProduto categoria = CategoriaProduto.valueOf(rs.getString("Categoria"));
        LocalDateTime dataCriacao = rs.getTimestamp("DataCriacao").toLocalDateTime();
        LocalDateTime dataAtualizacao = rs.getTimestamp("DataAtualizacao").toLocalDateTime();

        return new Produto(id, nome, descricao, preco, categoria, dataCriacao, dataAtualizacao);
    }

    public boolean insert(Produto produto) throws SQLException {
        String sql = "INSERT INTO Produtos (Nome, Descricao, Preco, Categoria, DataCriacao, DataAtualizacao) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPreco());
            stmt.setString(4, produto.getCategoria().name());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        produto.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean update(Produto produto) throws SQLException {
        String sql = "UPDATE Produtos SET Nome = ?, Descricao = ?, Preco = ?, Categoria = ?, DataAtualizacao = CURRENT_TIMESTAMP WHERE ProdutoID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPreco());
            stmt.setString(4, produto.getCategoria().name());
            stmt.setInt(5, produto.getId());

            return stmt.executeUpdate() > 0;
        }
    }
}
