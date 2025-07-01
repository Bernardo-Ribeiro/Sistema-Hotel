package com.hotel.gerenciador.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
    private BigDecimal valorTotal;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public Reserva(){}

    public Reserva(int id, Hospede hospede, Quarto quarto, LocalDate dataCheckIn, LocalDate dataCheckOut, 
                   StatusReserva status, BigDecimal valorTotal,
                   LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        setHospede(hospede);
        setQuarto(quarto);
        setDataCheckIn(dataCheckIn);
        setDataCheckOut(dataCheckOut);
        setStatus(status);
        
        if (valorTotal != null) {
            this.valorTotal = valorTotal;
        } else {
            this.valorTotal = calcularValorTotal();
        }
        
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
        this.valorTotal = calcularValorTotal();
    }

    public LocalDate getDataCheckIn() {
        return dataCheckIn;
    }
    public void setDataCheckIn(LocalDate dataCheckIn) {
        Validator.validateNotNull(dataCheckIn, "Data de check-in");
        //Validator.validateNotFutureDate(dataCheckIn);
        if (this.dataCheckOut != null) {
            Validator.validateDateRange(dataCheckIn, this.dataCheckOut);
        }
        this.dataCheckIn = dataCheckIn;
        this.valorTotal = calcularValorTotal();
    }

    public LocalDate getDataCheckOut() {
        return dataCheckOut;
    }
    public void setDataCheckOut(LocalDate dataCheckOut) {
        if (dataCheckOut == null) {
            throw new IllegalArgumentException("A data de check-out não pode ser nula.");
        }
        if (this.dataCheckIn != null && dataCheckOut.isBefore(this.dataCheckIn)) {
            throw new IllegalArgumentException("A data de check-out não pode ser anterior à data de check-in.");
        }
        if (this.dataCheckIn != null && dataCheckOut.isEqual(this.dataCheckIn)) {
            throw new IllegalArgumentException("A data de check-out não pode ser igual à data de check-in para o cálculo de diárias.");
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

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        if (valorTotal != null) {
            Validator.validatePositiveValue(valorTotal);
        }
        this.valorTotal = valorTotal;
    }

    public BigDecimal calcularValorTotal() {
        if (dataCheckIn == null || dataCheckOut == null || quarto == null || quarto.getPrecoDiaria() == null) {
            return BigDecimal.ZERO;
        }
        if (dataCheckOut.isBefore(dataCheckIn) || dataCheckOut.isEqual(dataCheckIn)) {
            return BigDecimal.ZERO;
        }
        
        long dias = ChronoUnit.DAYS.between(dataCheckIn, dataCheckOut);
        
        if (dias <= 0) {
            return BigDecimal.ZERO;
        }
        
        return quarto.getPrecoDiaria().multiply(new BigDecimal(dias));
    }

    public boolean isAtiva() {
        return this.status == StatusReserva.CONFIRMADA;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    public void setDataCriacao(LocalDateTime dataCriacao) {
        if (dataCriacao != null) {
            Validator.validateNotFutureDateTime(dataCriacao);
        }
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        if (dataAtualizacao != null) {
            Validator.validateNotFutureDateTime(dataAtualizacao);
        }
        this.dataAtualizacao = dataAtualizacao;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "id=" + id +
                ", hospede=" + (hospede != null ? hospede.getNome() : "null") +
                ", quarto=" + (quarto != null ? quarto.getNumeroQuarto() : "null") +
                ", dataCheckIn=" + (dataCheckIn != null ? Formatter.formatDate(dataCheckIn) : "null") +
                ", dataCheckOut=" + (dataCheckOut != null ? Formatter.formatDate(dataCheckOut) : "null") +
                ", status=" + status +
                ", valorTotal=" + Formatter.formatCurrency(valorTotal) +
                ", dataCriacao=" + (dataCriacao != null ? Formatter.formatDateTime(dataCriacao) : "null") +
                ", dataAtualizacao=" + (dataAtualizacao != null ? Formatter.formatDateTime(dataAtualizacao) : "null") +
                '}';
    }
}