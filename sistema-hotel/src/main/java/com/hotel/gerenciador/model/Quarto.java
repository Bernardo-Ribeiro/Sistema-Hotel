package com.hotel.gerenciador.model;

import java.time.LocalDateTime;

import com.hotel.gerenciador.util.StatusQuarto;
import com.hotel.gerenciador.util.TipoQuarto;
import com.hotel.gerenciador.util.Validator;
import com.hotel.gerenciador.util.Formatter;

public class Quarto {
    private int id;
    private int numeroQuarto;
    private TipoQuarto tipo;
    private double precoDiaria;
    private int capacidade;
    private StatusQuarto status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public Quarto() {}

    public Quarto(int id, int numeroQuarto, TipoQuarto tipo, double precoDiaria, int capacidade,
                  StatusQuarto status, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        setNumeroQuarto(numeroQuarto);
        setTipo(tipo);
        setPrecoDiaria(precoDiaria);
        setCapacidade(capacidade);
        setStatus(status);
        setDataCriacao(dataCriacao);
        setDataAtualizacao(dataAtualizacao);
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getNumeroQuarto() {
        return numeroQuarto;
    }
    public void setNumeroQuarto(int numeroQuarto) {
        this.numeroQuarto = numeroQuarto;
    }

    public TipoQuarto getTipo() {
        return tipo;
    }
    public void setTipo(TipoQuarto tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("O tipo do quarto não pode ser nulo.");
        }
        this.tipo = tipo;
    }

    public double getPrecoDiaria() {
        return precoDiaria;
    }
    public void setPrecoDiaria(double precoDiaria) {
        Validator.validatePositiveValue(precoDiaria);
        this.precoDiaria = precoDiaria;
    }

    public int getCapacidade() {
        return capacidade;
    }
    public void setCapacidade(int capacidade) {
        if (capacidade <= 0) {
            throw new IllegalArgumentException("A capacidade do quarto deve ser maior que zero.");
        }
        this.capacidade = capacidade;
    }

    public StatusQuarto getStatus() {
        return status;
    }
    public void setStatus(StatusQuarto status) {
        if (status == null) {
            throw new IllegalArgumentException("O status do quarto não pode ser nulo.");
        }
        this.status = status;
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

    public double calcularPrecoTotal(int dias) {
        return precoDiaria * dias;
    }

    public boolean isDisponivel() {
        return this.status == StatusQuarto.DISPONIVEL;
    }

    @Override
    public String toString() {
        return "Quarto{" +
                "id=" + id +
                ", numeroQuarto=" + numeroQuarto +
                ", tipo=" + tipo +
                ", precoDiaria=" + Formatter.formatCurrency(precoDiaria) +
                ", capacidade=" + capacidade +
                ", status=" + status +
                ", dataCriacao=" + Formatter.formatDateTime(dataCriacao) +
                ", dataAtualizacao=" + Formatter.formatDateTime(dataAtualizacao) +
                '}';
    }
}