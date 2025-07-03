package com.hotel.gerenciador.services;

import com.hotel.gerenciador.daos.ReservaDAO;
import com.hotel.gerenciador.models.Reserva;
import com.hotel.gerenciador.utils.StatusReserva;
import com.hotel.gerenciador.utils.Validator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
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


    public List<Reserva> findAll() {
        try {
            return reservaDAO.findAll();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todas as reservas: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList(); 
        }
    }

    public boolean verificarDisponibilidade(int quartoId, LocalDate dataCheckIn, LocalDate dataCheckOut) {
        Validator.validateDateRange(dataCheckIn, dataCheckOut);
        try {
            List<Reserva> reservasConflitantes = reservaDAO.findByQuartoAndPeriodo(quartoId, dataCheckIn, dataCheckOut);
            return reservasConflitantes.isEmpty();
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
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Reserva> findReservasAtivasPorQuarto(int quartoId) {
        try {
            return reservaDAO.findAtivasByQuarto(quartoId);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar reservas ativas de quarto: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
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

    public List<Reserva> buscarReservasComFiltros(StatusReserva status, LocalDate dataDe, LocalDate dataAte, String termoBusca) {
        try {
            if (dataDe != null && dataAte != null && dataDe.isAfter(dataAte)) {
                System.err.println("Erro no serviço: Data 'De' não pode ser posterior à data 'Até'.");
                return Collections.emptyList();
            }
            return reservaDAO.buscarComFiltros(status, dataDe, dataAte, termoBusca);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    public boolean verificarDisponibilidade(int quartoId, LocalDate dataCheckIn, LocalDate dataCheckOut, int reservaIdParaIgnorar) {
        Validator.validateDateRange(dataCheckIn, dataCheckOut);
        try {
            List<Reserva> reservasConflitantes = reservaDAO.findByQuartoAndPeriodo(quartoId, dataCheckIn, dataCheckOut, reservaIdParaIgnorar);
            return reservasConflitantes.isEmpty();
        } catch (SQLException e) {
            e.printStackTrace();
            return false; 
        } catch (IllegalArgumentException e) {
            System.err.println("verificarDisponibilidade: " + e.getMessage());
            return false;
        }
    }
}