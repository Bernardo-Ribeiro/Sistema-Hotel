package com.hotel.gerenciador.model;

import java.time.LocalDateTime;

import com.hotel.gerenciador.util.StatusQuarto;
import com.hotel.gerenciador.util.TipoQuarto;

public class Quarto {
    private int id;
    private int numeroQuarto;
    private TipoQuarto tipo;
    private double precoDiaria;
    private int capacidade;
    private StatusQuarto status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public Quarto(int id, int numeroQuarto, TipoQuarto tipo, double precoDiaria, int capacidade,
                    StatusQuarto status, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.numeroQuarto = numeroQuarto;
        this.tipo = tipo;
        this.precoDiaria = precoDiaria;
        this.capacidade = capacidade;
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
        this.tipo = tipo;
    }

    public double getPrecoDiaria() {
        return precoDiaria;
    }
    public void setPrecoDiaria(double precoDiaria) {
        this.precoDiaria = precoDiaria;
    }

    public int getCapacidade() {
        return capacidade;
    }
    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public StatusQuarto getStatus() {
        return status;
    }
    public void setStatus(StatusQuarto status) {
        this.status = status;
    }

    public double calcularPrecoTotal(int dias) {
        return precoDiaria * dias;
    }

    public boolean isDisponivel() {
        return this.status == StatusQuarto.DISPONIVEL;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    @Override
    public String toString() {
        return "Quarto{" +
                "id=" + id +
                ", numeroQuarto=" + numeroQuarto +
                ", tipo=" + tipo +
                ", precoDiaria=" + precoDiaria +
                ", capacidade=" + capacidade +
                ", status=" + status +
                ", dataCriacao=" + dataCriacao +
                ", dataAtualizacao=" + dataAtualizacao +
                '}';
    }
}
