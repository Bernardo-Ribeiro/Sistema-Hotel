package com.hotel.gerenciador.dao;

import com.hotel.gerenciador.model.Pagamento;
import com.hotel.gerenciador.util.MetodoPagamento;
import com.hotel.gerenciador.util.StatusPagamento;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PagamentoDAO extends BaseDAO<Pagamento> {

    @Override
    protected String getTableName() {
        return "Pagamentos";
    }
    protected String getIdColumnName() {
        return "PagamentoID";
    }

    @Override
    protected Pagamento fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("PagamentoID");
        double valorPago = rs.getDouble("ValorPago");
        LocalDateTime dataPagamento = rs.getTimestamp("DataPagamento").toLocalDateTime();
        MetodoPagamento metodoPagamento = MetodoPagamento.valueOf(rs.getString("MetodoPagamento"));
        StatusPagamento statusPagamento = StatusPagamento.valueOf(rs.getString("StatusPagamento"));
        String referencia = rs.getString("Referencia");
        LocalDateTime dataCriacao = rs.getTimestamp("DataCriacao").toLocalDateTime();
        LocalDateTime dataAtualizacao = rs.getTimestamp("DataAtualizacao").toLocalDateTime();

        return new Pagamento(id, valorPago, dataPagamento, metodoPagamento, statusPagamento, referencia, dataCriacao, dataAtualizacao);
    }

    public boolean insert(Pagamento pagamento) throws SQLException {
        String sql = "INSERT INTO Pagamentos (ReservaID, ValorPago, DataPagamento, MetodoPagamento, StatusPagamento, Referencia, DataCriacao, DataAtualizacao) " +
                     "VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, pagamento.getId());
            stmt.setDouble(2, pagamento.getValor());
            stmt.setTimestamp(3, Timestamp.valueOf(pagamento.getDataPagamento()));
            stmt.setString(4, pagamento.getMetodo().name());
            stmt.setString(5, pagamento.getStatus().name());
            stmt.setString(6, pagamento.getReferencia());

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
        String sql = "UPDATE Pagamentos SET ValorPago = ?, DataPagamento = ?, MetodoPagamento = ?, StatusPagamento = ?, Referencia = ?, DataAtualizacao = CURRENT_TIMESTAMP WHERE PagamentoID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, pagamento.getValor());
            stmt.setTimestamp(2, Timestamp.valueOf(pagamento.getDataPagamento()));
            stmt.setString(3, pagamento.getMetodo().name());
            stmt.setString(4, pagamento.getStatus().name());
            stmt.setString(5, pagamento.getReferencia());
            stmt.setInt(6, pagamento.getId());

            return stmt.executeUpdate() > 0;
        }
    }
    public List<Pagamento> findByStatus(StatusPagamento status) throws SQLException {
        String sql = "SELECT * FROM Pagamentos WHERE StatusPagamento = ?";
        List<Pagamento> pagamentos = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Pagamento pagamento = fromResultSet(rs);
                pagamentos.add(pagamento);
            }
        }

        return pagamentos;
    }
}
