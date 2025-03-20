package com.hotel.gerenciador.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hotel.gerenciador.util.MetodoPagamento;
import com.hotel.gerenciador.util.StatusPagamento;

public class Pagamento {
    private int id;
    private double valor;
    private LocalDate dataPagamento;
    private MetodoPagamento metodo;
    private StatusPagamento status;
    private String referencia;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public Pagamento(int id, double valor, LocalDate dataPagamento, MetodoPagamento metodo,
                    StatusPagamento status, String referencia, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.valor = valor;
        this.dataPagamento = dataPagamento;
        this.metodo = metodo;
        this.status = status;
        this.referencia = referencia;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public double getValor() {
        return valor;
    }
    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }
    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public MetodoPagamento getMetodo() {
        return metodo;
    }
    public void setMetodo(MetodoPagamento metodo) {
        this.metodo = metodo;
    }

    public StatusPagamento getStatus() {
        return status;
    }
    public void setStatus(StatusPagamento status) {
        this.status = status;
    }

    public String getReferencia() {
        return referencia;
    }
    public void setReferencia(String referencia) {
        this.referencia = referencia;
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
        return "Pagamento{" +
                "id=" + id +
                ", valor=" + valor +
                ", dataPagamento=" + dataPagamento +
                ", metodo=" + metodo +
                ", status=" + status +
                ", referencia='" + referencia + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", dataAtualizacao=" + dataAtualizacao +
                '}';
    }
}
