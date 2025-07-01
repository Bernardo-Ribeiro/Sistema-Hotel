package com.hotel.gerenciador.daos;

import com.hotel.gerenciador.models.Servico;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ServicoDAO extends BaseDAO<Servico> {

    @Override
    protected String getTableName() {
        return "servicos";
    }

    @Override
    protected String getIdColumnName() {
        return "ServicoID";
    }

    @Override
    protected Servico fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("ServicoID");
        String nome = rs.getString("Nome");
        String descricao = rs.getString("Descricao");
        BigDecimal preco = rs.getBigDecimal("Preco");
        boolean disponivel = rs.getBoolean("Disponivel");
        
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

        return new Servico(id, nome, descricao, preco, disponivel, dataCriacao, dataAtualizacao);
    }

    public boolean insert(Servico servico) throws SQLException {
        String sql = "INSERT INTO servicos (Nome, Descricao, Preco, Disponivel) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, servico.getNome());
            stmt.setString(2, servico.getDescricao());
            stmt.setBigDecimal(3, servico.getPreco());
            stmt.setBoolean(4, servico.isDisponivel());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        servico.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean update(Servico servico) throws SQLException {
        String sql = "UPDATE servicos SET Nome = ?, Descricao = ?, Preco = ?, Disponivel = ? WHERE ServicoID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, servico.getNome());
            stmt.setString(2, servico.getDescricao());
            stmt.setBigDecimal(3, servico.getPreco());
            stmt.setBoolean(4, servico.isDisponivel());
            stmt.setInt(5, servico.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int servicoId) throws SQLException {
        String sql = "DELETE FROM servicos WHERE ServicoID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, servicoId);
            return stmt.executeUpdate() > 0;
        }
    }

    public Servico findById(int id) throws SQLException {
        String sql = "SELECT * FROM servicos WHERE ServicoID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return fromResultSet(rs);
                }
            }
        }
        return null;
    }
}