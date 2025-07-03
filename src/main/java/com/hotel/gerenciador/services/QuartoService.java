package com.hotel.gerenciador.services;

import com.hotel.gerenciador.daos.QuartoDAO;
import com.hotel.gerenciador.models.Quarto;
import com.hotel.gerenciador.models.Reserva;
import com.hotel.gerenciador.utils.StatusQuarto;
import com.hotel.gerenciador.utils.TipoQuarto;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class QuartoService {

    private final QuartoDAO quartoDAO;
    private final ReservaService reservaService;

    public QuartoService() {
        this.quartoDAO = new QuartoDAO();
        this.reservaService = new ReservaService();
    }

    public boolean addQuarto(Quarto quarto) {
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
            return Collections.emptyList();
        }
    }

    public boolean upQuarto(Quarto quarto) {
        if (quarto.getPrecoDiaria() == null || quarto.getPrecoDiaria().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço da diária deve ser maior que zero.");
        }
        if (quarto.getNumeroQuarto() <= 0) {
             throw new IllegalArgumentException("O número do quarto deve ser positivo.");
        }
        if (quarto.getTipo() == null) {
            throw new IllegalArgumentException("O tipo do quarto não pode ser nulo.");
        }
         if (quarto.getStatus() == null) {
            throw new IllegalArgumentException("O status do quarto não pode ser nulo.");
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
        if (novoStatus == null) {
            throw new IllegalArgumentException("O novo status não pode ser nulo.");
        }
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

    public List<Quarto> findByTipo(TipoQuarto tipo) {
        if (tipo == null) {
            System.err.println("Tentativa de buscar quartos com tipo nulo.");
            return Collections.emptyList();
        }
        try {
            return quartoDAO.findByTipo(tipo); 
        } catch (SQLException e) {
            System.err.println("Erro ao buscar quartos por tipo (" + tipo + "): " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    public Quarto findQuartoByNumero(int numeroQuarto) {
        if (numeroQuarto <= 0) {
            System.err.println("Tentativa de buscar quarto com número inválido: " + numeroQuarto);
            new IllegalArgumentException("O número do quarto deve ser positivo.");
            return null;
        }
        try {
            return quartoDAO.findByNumero(numeroQuarto);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar quarto pelo número " + numeroQuarto + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}