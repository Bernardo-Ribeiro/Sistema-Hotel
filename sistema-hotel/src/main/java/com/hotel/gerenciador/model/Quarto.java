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
    private StatusQuarto status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public Quarto() {}

    public Quarto(int id, int numeroQuarto, TipoQuarto tipo, double precoDiaria,
                  StatusQuarto status, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.numeroQuarto = numeroQuarto;
        this.tipo = tipo;
        this.precoDiaria = precoDiaria;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
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
                ", status=" + status +
                ", dataCriacao=" + Formatter.formatDateTime(dataCriacao) +
                ", dataAtualizacao=" + Formatter.formatDateTime(dataAtualizacao) +
                '}';
    }
}