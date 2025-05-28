package com.hotel.gerenciador.service;

import com.hotel.gerenciador.dao.QuartoDAO;
import com.hotel.gerenciador.model.Quarto;
import com.hotel.gerenciador.model.Reserva;
import com.hotel.gerenciador.util.StatusQuarto;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class QuartoService {

    private final QuartoDAO quartoDAO;
    private final ReservaService reservaService;

    public QuartoService() {
        this.quartoDAO = new QuartoDAO();
        this.reservaService = new ReservaService();
    }

    public boolean addQuarto(Quarto quarto) {
        if (quarto.getPrecoDiaria() == null || quarto.getPrecoDiaria().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço da diária deve ser maior que zero.");
        }

        try {
            return quartoDAO.insert(quarto);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Quarto> findAll() {
        try {
            return quartoDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean upQuarto(Quarto quarto) {
        if (quarto.getPrecoDiaria() == null || quarto.getPrecoDiaria().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço da diária deve ser maior que zero.");
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
            List<Reserva> reservas = reservaService.findReservasAtivasPorQuarto(quartoId);
            if (reservas != null && !reservas.isEmpty()) {
                throw new IllegalStateException("Não é possível deletar um quarto com reservas ativas.");
            }
            return quartoDAO.delete(quartoId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalStateException e) {
            System.err.println("Erro ao deletar quarto: " + e.getMessage());
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
        } catch (IllegalStateException e) {
            System.err.println("Erro ao alterar status do quarto: " + e.getMessage());
            return false;
        }
    }
}