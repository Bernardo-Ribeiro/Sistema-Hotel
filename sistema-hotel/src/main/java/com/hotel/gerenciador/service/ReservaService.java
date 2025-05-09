package com.hotel.gerenciador.service;

import com.hotel.gerenciador.dao.ReservaDAO;
import com.hotel.gerenciador.model.Reserva;
import com.hotel.gerenciador.util.StatusReserva;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

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
}
