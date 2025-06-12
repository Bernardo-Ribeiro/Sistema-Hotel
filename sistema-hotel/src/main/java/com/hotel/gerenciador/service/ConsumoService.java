package com.hotel.gerenciador.service;

import com.hotel.gerenciador.dao.ConsumoDAO;
import com.hotel.gerenciador.model.Consumo;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ConsumoService {

    private final ConsumoDAO consumoDAO;

    public ConsumoService() {
        this.consumoDAO = new ConsumoDAO();
    }

    public boolean addConsumo(Consumo consumo) {
        try {
            consumoDAO.insert(consumo);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateConsumo(Consumo consumo) {
        try {
            return consumoDAO.update(consumo);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteConsumo(int consumoId) {
        try {
            return consumoDAO.delete(consumoId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Consumo findConsumoPorId(int consumoId) {
        try {
            return consumoDAO.findById(consumoId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Consumo> findConsumosPorReserva(int reservaId) {
        try {
            return consumoDAO.findByReservaId(reservaId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Consumo> getAllConsumos() {
        try {
            return consumoDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public BigDecimal calcularTotalConsumos(int reservaId) {
        try {
            List<Consumo> consumos = consumoDAO.findByReservaId(reservaId);
            return consumos.stream()
                .map(c -> BigDecimal.valueOf(c.getValor()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch (SQLException e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
}
