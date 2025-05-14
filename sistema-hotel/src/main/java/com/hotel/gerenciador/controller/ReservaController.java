package com.hotel.gerenciador.controller;

import com.hotel.gerenciador.model.Reserva;
import com.hotel.gerenciador.service.ReservaService;
import com.hotel.gerenciador.util.StatusReserva;
import com.hotel.gerenciador.util.Validator;

import java.time.LocalDate;
import java.util.List;

public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController() {
        this.reservaService = new ReservaService();
    }

    public boolean cadastrarReserva(Reserva reserva) {
        return reservaService.addReserva(reserva);
    }

    public boolean atualizarReserva(Reserva reserva) {
        return reservaService.upReserva(reserva);
    }

    public boolean deletarReserva(int reservaId) {
        return reservaService.delReserva(reservaId);
    }

    public Reserva buscarReservaPorId(int reservaId) {
        return reservaService.findReservaPorId(reservaId);
    }

    public List<Reserva> listarReservasPorStatus(StatusReserva status) {
        return reservaService.findReservasPorStatus(status);
    }
    
    public boolean verificarDisponibilidade(int quartoId, LocalDate dataCheckIn, LocalDate dataCheckOut) {
        try {
            Validator.validateDateRange(dataCheckIn, dataCheckOut);
            boolean disponivel = reservaService.verificarDisponibilidade(quartoId, dataCheckIn, dataCheckOut);
            Validator.validateDisponibilidade(disponivel);
            return disponivel;
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    public List<Reserva> listarReservasPorHospede(int hospedeId) {
        return reservaService.findReservasPorHospede(hospedeId);
    }

    public List<Reserva> listarReservasAtivasPorQuarto(int quartoId) {
        return reservaService.findReservasAtivasPorQuarto(quartoId);
    }
}