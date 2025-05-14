package com.hotel.gerenciador.controller;

import com.hotel.gerenciador.model.Quarto;
import com.hotel.gerenciador.service.QuartoService;
import com.hotel.gerenciador.util.StatusQuarto;

public class QuartoController {

    private final QuartoService quartoService;

    public QuartoController() {
        this.quartoService = new QuartoService();
    }

    public boolean cadastrarQuarto(Quarto quarto) {
        return quartoService.addQuarto(quarto);
    }

    public boolean atualizarQuarto(Quarto quarto) {
        return quartoService.upQuarto(quarto);
    }

    public boolean deletarQuarto(int quartoId) {
        return quartoService.delQuarto(quartoId);
    }

    public Quarto buscarQuartoPorId(int quartoId) {
        return quartoService.findQuartoPorId(quartoId);
    }

    public boolean alterarStatusQuarto(int quartoId, StatusQuarto novoStatus) {
        return quartoService.altStatusQuarto(quartoId, novoStatus);
    }
}