package com.hotel.gerenciador.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hotel.gerenciador.util.MetodoPagamento;
import com.hotel.gerenciador.util.StatusReserva;

public class Reserva {
    private int id;
    private Hospede hospede;
    private Quarto quarto;
    private LocalDate dataCheckIn;
    private LocalDate dataCheckOut;
    private StatusReserva status;
    private double valorTotal;
    private MetodoPagamento metodoPagamento;
    private String observacoes;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public Reserva(int id, Hospede hospede, Quarto quarto, LocalDate dataCheckIn, LocalDate dataCheckOut,
                    StatusReserva status, MetodoPagamento metodoPagamento, String observacoes,
                    LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.hospede = hospede;
        this.quarto = quarto;
        this.dataCheckIn = dataCheckIn;
        this.dataCheckOut = dataCheckOut;
        this.status = status;
        this.metodoPagamento = metodoPagamento;
        this.observacoes = observacoes;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.valorTotal = calcularValorTotal();
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
        this.hospede = hospede;
    }

    public Quarto getQuarto() {
        return quarto;
    }
    public void setQuarto(Quarto quarto) {
        this.quarto = quarto;
    }

    public LocalDate getDataCheckIn() {
        return dataCheckIn;
    }
    public void setDataCheckIn(LocalDate dataCheckIn) {
        this.dataCheckIn = dataCheckIn;
    }

    public LocalDate getDataCheckOut() {
        return dataCheckOut;
    }
    public void setDataCheckOut(LocalDate dataCheckOut) {
        this.dataCheckOut = dataCheckOut;
    }

    public StatusReserva getStatus() {
        return status;
    }
    public void setStatus(StatusReserva status) {
        this.status = status;
    }

    public double getValorTotal() { return valorTotal; }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }
    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public String getObservacoes() {
        return observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public double calcularValorTotal() {
        int dias = (int) (dataCheckOut.toEpochDay() - dataCheckIn.toEpochDay());
        return quarto.getPrecoDiaria() * dias;
    }

    public boolean isAtiva() {
        return this.status == StatusReserva.CONFIRMADA;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "id=" + id +
                ", hospede=" + hospede.getNome() +
                ", quarto=" + quarto.getNumeroQuarto() +
                ", dataCheckIn=" + dataCheckIn +
                ", dataCheckOut=" + dataCheckOut +
                ", status=" + status +
                ", valorTotal=" + valorTotal +
                ", metodoPagamento=" + metodoPagamento +
                ", observacoes='" + observacoes + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", dataAtualizacao=" + dataAtualizacao +
                '}';
    }
}
