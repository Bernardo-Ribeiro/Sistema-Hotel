package com.hotel.gerenciador.controller;

import com.hotel.gerenciador.model.Pagamento;
import com.hotel.gerenciador.service.PagamentoService;
import com.hotel.gerenciador.util.StatusPagamento;

import java.util.List;

public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController() {
        this.pagamentoService = new PagamentoService();
    }

    public boolean cadastrarPagamento(Pagamento pagamento) {
        return pagamentoService.addPagamento(pagamento);
    }

    public boolean atualizarPagamento(Pagamento pagamento) {
        return pagamentoService.upPagamento(pagamento);
    }

    public boolean deletarPagamento(int pagamentoId) {
        return pagamentoService.delPagamento(pagamentoId);
    }

    public Pagamento buscarPagamentoPorId(int pagamentoId) {
        return pagamentoService.findPagamentoPorId(pagamentoId);
    }

    public List<Pagamento> buscarPagamentosPorStatus(StatusPagamento status) {
        return pagamentoService.findPagamentosPorStatus(status);
    }
}