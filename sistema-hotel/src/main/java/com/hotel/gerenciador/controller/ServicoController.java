package com.hotel.gerenciador.controller;

import com.hotel.gerenciador.model.Servico;
import com.hotel.gerenciador.service.ServicoService;

public class ServicoController {

    private final ServicoService servicoService;

    public ServicoController() {
        this.servicoService = new ServicoService();
    }
    
    public boolean cadastrarServico(Servico servico) {
        if (servico.getPreco() < 20.0) {
            throw new IllegalArgumentException("O preço do serviço deve ser superior a 20 reais.");
        }
        return servicoService.addServico(servico);
    }

    public boolean atualizarServico(Servico servico) {
        if (servico.getPreco() < 0) {
            throw new IllegalArgumentException("O preço do serviço não pode ser negativo.");
        }
        return servicoService.upServico(servico);
    }

    public boolean deletarServico(int servicoId) {
        return servicoService.delServico(servicoId);
    }

    public Servico buscarServicoPorId(int servicoId) {
        return servicoService.findServicoPorId(servicoId);
    }
}
