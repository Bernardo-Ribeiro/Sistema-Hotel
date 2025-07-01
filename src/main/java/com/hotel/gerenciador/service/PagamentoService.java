package com.hotel.gerenciador.service;

import com.hotel.gerenciador.dao.PagamentoDAO;
import com.hotel.gerenciador.model.Pagamento;
import com.hotel.gerenciador.util.StatusPagamento;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PagamentoService {

    private final PagamentoDAO pagamentoDAO;

    public PagamentoService() {
        this.pagamentoDAO = new PagamentoDAO();
    }

    public boolean addPagamento(Pagamento pagamento) {
        try {
            return pagamentoDAO.insert(pagamento);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean upPagamento(Pagamento pagamento) {
        if (pagamento.getStatus() == StatusPagamento.PAGO) {
            throw new IllegalArgumentException("Não é possível alterar um pagamento com status CONCLUÍDO.");
        }

        try {
            return pagamentoDAO.update(pagamento);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delPagamento(int pagamentoId) {
        try {
            Pagamento pagamentoExistente = pagamentoDAO.findById(pagamentoId);
            if (pagamentoExistente != null && pagamentoExistente.getStatus() == StatusPagamento.PAGO) {
                throw new IllegalStateException("Não é possível deletar um pagamento já efetuado.");
            }
            return pagamentoDAO.delete(pagamentoId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Pagamento findPagamentoPorId(int pagamentoId) {
        try {
            return pagamentoDAO.findById(pagamentoId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Pagamento> findPagamentosPorStatus(StatusPagamento status) {
        try {
            return pagamentoDAO.findByStatus(status);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<Pagamento> getPagamentosPorReservaId(int reservaId) {
        try {
            return pagamentoDAO.findByReservaId(reservaId);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public BigDecimal calcularTotalPagoParaReserva(int reservaId) {
        try {
            List<Pagamento> pagamentos = pagamentoDAO.findByReservaId(reservaId);
            return pagamentos.stream()
                .map(Pagamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch (SQLException e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
}