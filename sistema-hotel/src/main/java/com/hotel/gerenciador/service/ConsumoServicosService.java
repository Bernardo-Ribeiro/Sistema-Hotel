package com.hotel.gerenciador.service;

import com.hotel.gerenciador.dao.ConsumoServicosDAO;
import com.hotel.gerenciador.model.ConsumoServicos;

import java.sql.SQLException;
import java.util.List;

public class ConsumoServicosService {

    private final ConsumoServicosDAO consumoServicosDAO;

    public ConsumoServicosService() {
        this.consumoServicosDAO = new ConsumoServicosDAO();
    }

    public boolean addConsumo(ConsumoServicos consumo) {
        if (consumo.getQuantidade() <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
    
        try {
            consumoServicosDAO.insert(consumo);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }    

    public boolean upConsumo(ConsumoServicos consumo) {
        if (consumo.getQuantidade() <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

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
}
