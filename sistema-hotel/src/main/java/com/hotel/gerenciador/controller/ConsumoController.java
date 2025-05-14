package com.hotel.gerenciador.controller;

import com.hotel.gerenciador.model.Consumo;
import com.hotel.gerenciador.service.ConsumoService;

import java.util.List;

public class ConsumoController {

    private final ConsumoService consumoService;

    public ConsumoController() {
        this.consumoService = new ConsumoService();
    }

    public boolean cadastrarConsumo(Consumo consumo) {
        return consumoService.addConsumo(consumo);
    }

    public boolean atualizarConsumo(Consumo consumo) {
        return consumoService.updateConsumo(consumo);
    }

    public boolean removerConsumo(int id) {
        return consumoService.deleteConsumo(id);
    }

    public Consumo buscarConsumoPorId(int id) {
        return consumoService.findConsumoPorId(id);
    }

    public List<Consumo> buscarConsumosPorReserva(int reservaId) {
        return consumoService.findConsumosPorReserva(reservaId);
    }

    public List<Consumo> listarTodosConsumos() {
        return consumoService.getAllConsumos();
    }
}
