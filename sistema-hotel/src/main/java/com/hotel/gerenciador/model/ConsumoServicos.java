package com.hotel.gerenciador.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ConsumoServicos {
    private int id;
    private int reservaId;
    private int servicoId;
    private int quantidade;
    private double valor;
    private LocalDate dataConsumo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public ConsumoServicos(int id, int reservaId, int servicoId, int quantidade, double valor,
                LocalDate dataConsumo, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.reservaId = reservaId;
        this.servicoId = servicoId;
        this.quantidade = quantidade;
        this.valor = valor;
        this.dataConsumo = dataConsumo;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReservaId() {
        return reservaId;
    }

    public void setReservaId(int reservaId) {
        this.reservaId = reservaId;
    }

    public int getServicoId() {
        return servicoId;
    }

    public void setServicoId(int servicoId) {
        this.servicoId = servicoId;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDate getDataConsumo() {
        return dataConsumo;
    }

    public void setDataConsumo(LocalDate dataConsumo) {
        this.dataConsumo = dataConsumo;
    }
    
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    @Override
    public String toString() {
        return "ConsumoServicos{" +
                "id=" + id +
                ", reservaId=" + reservaId +
                ", servicoId=" + servicoId +
                ", quantidade=" + quantidade +
                ", valor=" + valor +
                ", dataConsumo=" + dataConsumo +
                ", dataCriacao=" + dataCriacao +
                ", dataAtualizacao=" + dataAtualizacao +
                '}';
    }
}
