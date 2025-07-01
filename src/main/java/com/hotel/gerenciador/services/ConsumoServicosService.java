package com.hotel.gerenciador.services;

import com.hotel.gerenciador.daos.ConsumoServicosDAO;
import com.hotel.gerenciador.models.ConsumoServicos;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ConsumoServicosService {

    private final ConsumoServicosDAO consumoServicosDAO;
    private final ServicoService servicoService;

    public ConsumoServicosService() {
        this.consumoServicosDAO = new ConsumoServicosDAO();
        this.servicoService = new ServicoService();
    }

    public boolean addConsumo(ConsumoServicos consumo) {
        try {
            consumoServicosDAO.insert(consumo);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }    

    public boolean upConsumo(ConsumoServicos consumo) {
        try {
            return consumoServicosDAO.update(consumo);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delConsumo(int consumoId) {
        try {
            return consumoServicosDAO.delete(consumoId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ConsumoServicos findConsumoPorId(int consumoId) {
        try {
            return consumoServicosDAO.findById(consumoId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ConsumoServicos> findConsumosPorReserva(int reservaId) {
        try {
            return consumoServicosDAO.findByReservaId(reservaId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ConsumoServicos> getAllConsumos() {
        try {
            return consumoServicosDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public BigDecimal calcularTotalConsumos(int reservaId) {
        try {
            List<ConsumoServicos> consumos = consumoServicosDAO.findByReservaId(reservaId);
            return consumos.stream()
                .<BigDecimal>map(c -> BigDecimal.valueOf(c.getQuantidade() * servicoService.findServicoPorId(c.getServicoId()).getPreco().doubleValue()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch (SQLException e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
}
