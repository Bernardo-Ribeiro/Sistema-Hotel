package com.hotel.gerenciador.service;

import com.hotel.gerenciador.dao.ReservaDAO;
import com.hotel.gerenciador.model.Reserva;
import com.hotel.gerenciador.util.StatusReserva;
import com.hotel.gerenciador.util.Validator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservaService {

    private final ReservaDAO reservaDAO;

    public ReservaService() {
        this.reservaDAO = new ReservaDAO();
    }

    public boolean addReserva(Reserva reserva) {
        if (reserva.getDataCheckIn().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data de entrada não pode ser no passado.");
        }

        if (reserva.getStatus() == StatusReserva.CONCLUIDA) {
            throw new IllegalArgumentException("A reserva não pode ser criada com status CONCLUÍDA.");
        }

        try {
            return reservaDAO.insert(reserva);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean upReserva(Reserva reserva) {
        if (reserva.getStatus() == StatusReserva.CONCLUIDA) {
            throw new IllegalArgumentException("Não é possível alterar uma reserva com status CONCLUÍDA.");
        }

        try {
            return reservaDAO.update(reserva);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delReserva(int reservaId) {
        try {
            return reservaDAO.delete(reservaId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Reserva findReservaPorId(int reservaId) {
        try {
            return reservaDAO.findById(reservaId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Reserva> findReservasPorStatus(StatusReserva status) {
        try {
            return reservaDAO.findByStatus(status);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean verificarDisponibilidade(int quartoId, LocalDate dataCheckIn, LocalDate dataCheckOut) {
        Validator.validateDateRange(dataCheckIn, dataCheckOut);
        
        try {
            List<Reserva> reservasConflitantes = reservaDAO.findByQuartoAndPeriodo(
                quartoId, 
                dataCheckIn, 
                dataCheckOut
            );
            
            return reservasConflitantes.stream()
                .noneMatch(r -> r.getStatus() != StatusReserva.CANCELADA && 
                            r.getStatus() != StatusReserva.PENDENTE);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Reserva> findReservasPorHospede(int hospedeId) {
        try {
            return reservaDAO.findByHospede(hospedeId);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar reservas de hóspede: " + e.getMessage());
            return null;
        }
    }

    public List<Reserva> findReservasAtivasPorQuarto(int quartoId) {
        try {
            return reservaDAO.findAtivasByQuarto(quartoId);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar reservas ativas de quarto: " + e.getMessage());
            return null;
        }
    }
    public Map<Integer, Reserva> findReservasAtivasMapPorQuarto() {
        try {
            List<Reserva> reservasAtivas = reservaDAO.findAtivas();
            Map<Integer, Reserva> mapa = new HashMap<>();
            
            for (Reserva r : reservasAtivas) {
                if (r.getQuarto() != null) {
                    System.out.println("Adicionando ao mapa: Quarto ID=" + r.getQuarto().getId() + 
                                    ", Número=" + r.getQuarto().getNumeroQuarto() + 
                                    ", Reserva ID=" + r.getId() + 
                                    ", Hóspede=" + (r.getHospede() != null ? r.getHospede().getNome() : "null"));
                    
                    mapa.put(r.getQuarto().getId(), r);
                } else {
                    System.out.println("Reserva ID=" + r.getId() + " sem quarto associado - ignorada");
                }
            }
            return mapa;
        } catch (SQLException e) {
            System.err.println("Erro ao buscar reservas ativas: " + e.getMessage());
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}
