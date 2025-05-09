package com.hotel.gerenciador.service;

import com.hotel.gerenciador.dao.QuartoDAO;
import com.hotel.gerenciador.model.Quarto;
import com.hotel.gerenciador.util.StatusQuarto;

import java.sql.SQLException;

public class QuartoService {

    private final QuartoDAO quartoDAO;

    public QuartoService() {
        this.quartoDAO = new QuartoDAO();
    }

    public boolean addQuarto(Quarto quarto) {
        if (quarto.getPrecoDiaria() <= 0) {
            throw new IllegalArgumentException("O preço da diária deve ser maior que zero.");
        }

        if (quarto.getCapacidade() <= 0) {
            throw new IllegalArgumentException("A capacidade do quarto deve ser maior que zero.");
        }

        try {
            return quartoDAO.insert(quarto);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean upQuarto(Quarto quarto) {
        if (quarto.getPrecoDiaria() <= 0) {
            throw new IllegalArgumentException("O preço da diária deve ser maior que zero.");
        }

        if (quarto.getCapacidade() <= 0) {
            throw new IllegalArgumentException("A capacidade do quarto deve ser maior que zero.");
        }

        try {
            return quartoDAO.update(quarto);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delQuarto(int quartoId) {
        try {
            return quartoDAO.delete(quartoId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Quarto findQuartoPorId(int quartoId) {
        try {
            return quartoDAO.findById(quartoId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean altStatusQuarto(int quartoId, StatusQuarto novoStatus) {
        try {
            Quarto quarto = quartoDAO.findById(quartoId);
            if (quarto != null) {
                quarto.setStatus(novoStatus);
                return quartoDAO.update(quarto);
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
