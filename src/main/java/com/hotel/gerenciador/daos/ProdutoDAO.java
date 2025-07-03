package com.hotel.gerenciador.daos;

import com.hotel.gerenciador.models.Produto;
import com.hotel.gerenciador.utils.CategoriaProduto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ProdutoDAO extends BaseDAO<Produto> {

    @Override
    protected String getTableName() {
        return "produtos";
    }

    @Override
    protected String getIdColumnName() {
        return "ProdutoID";
    }

    @Override
    protected Produto fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("ProdutoID");
        String nome = rs.getString("Nome");
        String descricao = rs.getString("Descricao");
        BigDecimal preco = rs.getBigDecimal("Preco");
        int estoque = rs.getInt("Estoque");
        CategoriaProduto categoria = CategoriaProduto.valueOf(rs.getString("Categoria"));
        
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
        return new Produto(id, nome, descricao, preco, estoque, categoria, dataCriacao, dataAtualizacao);
    }

    public boolean insert(Produto produto) throws SQLException {
        String sql = "INSERT INTO produtos (Nome, Descricao, Preco, Estoque, Categoria, DataCriacao, DataAtualizacao) " +
                     "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setBigDecimal(3, produto.getPreco());
            stmt.setInt(4, produto.getEstoque());
            stmt.setString(5, produto.getCategoria().name());

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
        String sql = "UPDATE produtos SET Nome = ?, Descricao = ?, Preco = ?, Estoque = ?, Categoria = ?, DataAtualizacao = CURRENT_TIMESTAMP " +
                     "WHERE ProdutoID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setBigDecimal(3, produto.getPreco());
            stmt.setInt(4, produto.getEstoque());
            stmt.setString(5, produto.getCategoria().name());
            stmt.setInt(6, produto.getId());

            return stmt.executeUpdate() > 0;
        }
    }
}