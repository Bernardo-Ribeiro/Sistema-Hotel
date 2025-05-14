package com.hotel.gerenciador.controller;

import com.hotel.gerenciador.model.ConsumoServicos;
import com.hotel.gerenciador.service.ConsumoServicosService;

import java.util.List;

public class ConsumoServicosController {

    private final ConsumoServicosService consumoServicosService;

    public ConsumoServicosController() {
        this.consumoServicosService = new ConsumoServicosService();
    }

    public boolean cadastrarConsumo(ConsumoServicos consumo) {
        return consumoServicosService.addConsumo(consumo);
    }

    public boolean atualizarConsumo(ConsumoServicos consumo) {
        return consumoServicosService.upConsumo(consumo);
    }

    public boolean removerConsumo(int id) {
        return consumoServicosService.delConsumo(id);
    }

    public ConsumoServicos buscarConsumoPorId(int id) {
        return consumoServicosService.findConsumoPorId(id);
    }

    public List<ConsumoServicos> buscarConsumosPorReserva(int reservaId) {
        return consumoServicosService.findConsumosPorReserva(reservaId);
    }

    public List<ConsumoServicos> listarTodosConsumos() {
        return consumoServicosService.getAllConsumos();
    }
}
