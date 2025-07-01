package com.hotel.gerenciador.daos;

import com.hotel.gerenciador.models.Pagamento;
import com.hotel.gerenciador.utils.MetodoPagamento;
import com.hotel.gerenciador.utils.StatusPagamento;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PagamentoDAO extends BaseDAO<Pagamento> {

    @Override
    protected String getTableName() {
        return "pagamentos";
    }

    @Override
    protected String getIdColumnName() {
        return "PagamentoID";
    }

    @Override
    protected Pagamento fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("PagamentoID");
        BigDecimal valorPago = rs.getBigDecimal("ValorPago");
        LocalDate dataPagamento = rs.getDate("DataPagamento").toLocalDate();
        MetodoPagamento metodoPagamento = MetodoPagamento.valueOf(rs.getString("MetodoPagamento"));
        StatusPagamento statusPagamento = StatusPagamento.valueOf(rs.getString("StatusPagamento"));
        int reservaId = rs.getInt("ReservaID");

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
        
        return new Pagamento(id, valorPago, dataPagamento, metodoPagamento, statusPagamento, reservaId, dataCriacao, dataAtualizacao);
    }

    public boolean insert(Pagamento pagamento) throws SQLException {
        String sql = "INSERT INTO pagamentos (ReservaID, ValorPago, DataPagamento, MetodoPagamento, StatusPagamento) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, pagamento.getReservaId());
            stmt.setBigDecimal(2, pagamento.getValor());
            stmt.setDate(3, Date.valueOf(pagamento.getDataPagamento()));
            stmt.setString(4, pagamento.getMetodo().name());
            stmt.setString(5, pagamento.getStatus().name());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pagamento.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean update(Pagamento pagamento) throws SQLException {
        String sql = "UPDATE pagamentos SET ReservaID = ?, ValorPago = ?, DataPagamento = ?, MetodoPagamento = ?, StatusPagamento = ? " +
                     "WHERE PagamentoID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, pagamento.getReservaId());
            stmt.setBigDecimal(2, pagamento.getValor());
            stmt.setDate(3, Date.valueOf(pagamento.getDataPagamento()));
            stmt.setString(4, pagamento.getMetodo().name());
            stmt.setString(5, pagamento.getStatus().name());
            stmt.setInt(6, pagamento.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    public List<Pagamento> findByStatus(StatusPagamento status) throws SQLException {
        String sql = "SELECT * FROM pagamentos WHERE StatusPagamento = ?";
        List<Pagamento> pagamentos = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.name());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                pagamentos.add(fromResultSet(rs));
            }
        }
        return pagamentos;
    }
    public List<Pagamento> findByReservaId(int reservaId) throws SQLException {
        List<Pagamento> pagamentos = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName() + " WHERE ReservaID = ?";
        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pagamentos.add(fromResultSet(rs));
                }
            }
        }
        return pagamentos;
    }
}