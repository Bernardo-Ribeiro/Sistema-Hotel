package com.hotel.gerenciador.controller;

import com.hotel.gerenciador.model.Manutencao;
import com.hotel.gerenciador.service.ManutencaoService;
import com.hotel.gerenciador.util.StatusManutencao;

import java.util.List;

public class ManutencaoController {

    private final ManutencaoService manutencaoService;

    public ManutencaoController() {
        this.manutencaoService = new ManutencaoService();
    }

    public boolean cadastrarManutencao(Manutencao manutencao) {
        return manutencaoService.addManutencao(manutencao);
    }

    public boolean atualizarStatusManutencao(int idManutencao, StatusManutencao status) {
        return manutencaoService.upStatus(idManutencao, status);
    }

    public List<Manutencao> listarManutencoesPorStatus(StatusManutencao status) {
        return manutencaoService.listManutencaoPorStatus(status);
    }

    public Manutencao buscarManutencaoPorId(int idManutencao) {
        return manutencaoService.findManutencaoPorId(idManutencao);
    }
}
