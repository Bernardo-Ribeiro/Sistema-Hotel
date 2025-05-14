package com.hotel.gerenciador.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hotel.gerenciador.util.StatusReserva;
import com.hotel.gerenciador.util.Validator;
import com.hotel.gerenciador.util.Formatter;

public class Reserva {
    private int id;
    private Hospede hospede;
    private Quarto quarto;
    private LocalDate dataCheckIn;
    private LocalDate dataCheckOut;
    private StatusReserva status;
    private double valorTotal;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public Reserva(int id, int hospedeId, int quartoId, LocalDate dataCheckIn, LocalDate dataCheckOut,
               StatusReserva status, double valorTotal, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        
        this.hospede = new Hospede();
        this.hospede.setId(hospedeId);

        this.quarto = new Quarto();
        this.quarto.setId(quartoId);

        this.dataCheckIn = dataCheckIn;
        this.dataCheckOut = dataCheckOut;
        this.status = status;
        this.valorTotal = valorTotal;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }



    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Hospede getHospede() {
        return hospede;
    }
    public void setHospede(Hospede hospede) {
        if (hospede == null) {
            throw new IllegalArgumentException("Hospede não pode ser nulo.");
        }
        this.hospede = hospede;
    }

    public Quarto getQuarto() {
        return quarto;
    }
    public void setQuarto(Quarto quarto) {
        if (quarto == null) {
            throw new IllegalArgumentException("Quarto não pode ser nulo.");
        }
        this.quarto = quarto;
    }

    public LocalDate getDataCheckIn() {
        return dataCheckIn;
    }
    public void setDataCheckIn(LocalDate dataCheckIn) {
        Validator.validateNotFutureDate(dataCheckIn);
        if (this.dataCheckOut != null && dataCheckIn.isAfter(this.dataCheckOut)) {
            throw new IllegalArgumentException("A data de check-in não pode ser posterior à data de check-out.");
        }
        this.dataCheckIn = dataCheckIn;
        this.valorTotal = calcularValorTotal();
    }

    public LocalDate getDataCheckOut() {
        return dataCheckOut;
    }
    public void setDataCheckOut(LocalDate dataCheckOut) {
        Validator.validateNotFutureDate(dataCheckOut);
        if (this.dataCheckIn != null && dataCheckOut.isBefore(this.dataCheckIn)) {
            throw new IllegalArgumentException("A data de check-out não pode ser anterior à data de check-in.");
        }
        this.dataCheckOut = dataCheckOut;
        this.valorTotal = calcularValorTotal();
    }

    public StatusReserva getStatus() {
        return status;
    }
    public void setStatus(StatusReserva status) {
        if (status == null) {
            throw new IllegalArgumentException("Status da reserva não pode ser nulo.");
        }
        this.status = status;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public double calcularValorTotal() {
        if (dataCheckIn == null || dataCheckOut == null || quarto == null) return 0;
        int dias = (int) (dataCheckOut.toEpochDay() - dataCheckIn.toEpochDay());
        return quarto.getPrecoDiaria() * dias;
    }

    public boolean isAtiva() {
        return this.status == StatusReserva.CONFIRMADA;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    public void setDataCriacao(LocalDateTime dataCriacao) {
        Validator.validateNotFutureDateTime(dataCriacao);
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        Validator.validateNotFutureDateTime(dataAtualizacao);
        this.dataAtualizacao = dataAtualizacao;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "id=" + id +
                ", hospede=" + (hospede != null ? hospede.getNome() : "null") +
                ", quarto=" + (quarto != null ? quarto.getNumeroQuarto() : "null") +
                ", dataCheckIn=" + Formatter.formatDate(dataCheckIn) +
                ", dataCheckOut=" + Formatter.formatDate(dataCheckOut) +
                ", status=" + status +
                ", valorTotal=" + Formatter.formatCurrency(valorTotal) +
                ", dataCriacao=" + Formatter.formatDateTime(dataCriacao) +
                ", dataAtualizacao=" + Formatter.formatDateTime(dataAtualizacao) +
                '}';
    }
}
