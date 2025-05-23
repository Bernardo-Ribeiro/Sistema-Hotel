package com.hotel.gerenciador.service;

import com.hotel.gerenciador.dao.PagamentoDAO;
import com.hotel.gerenciador.model.Pagamento;
import com.hotel.gerenciador.util.StatusPagamento;

import java.sql.SQLException;
import java.util.List;

public class PagamentoService {

    private final PagamentoDAO pagamentoDAO;

    public PagamentoService() {
        this.pagamentoDAO = new PagamentoDAO();
    }

    public boolean addPagamento(Pagamento pagamento) {
        if (pagamento.getValor() <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser maior que zero.");
        }

        if (pagamento.getStatus() == StatusPagamento.PAGO) {
            throw new IllegalArgumentException("O pagamento já está concluído.");
        }

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
}
