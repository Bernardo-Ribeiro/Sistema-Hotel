package com.hotel.gerenciador.model;

import java.time.LocalDate;

public class Pagamento {
    private int id;
    private double valor;
    private LocalDate dataPagamento;
    private MetodoPagamento metodo;
    private StatusPagamento status;
    private String referencia;

    public Pagamento(int id, double valor, LocalDate dataPagamento, MetodoPagamento metodo, StatusPagamento status, String referencia) {
        this.id = id;
        this.valor = valor;
        this.dataPagamento = dataPagamento;
        this.metodo = metodo;
        this.status = status;
        this.referencia = referencia;
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

    @Override
    public String toString() {
        return "Pagamento{" +
                "id=" + id +
                ", valor=" + valor +
                ", dataPagamento=" + dataPagamento +
                ", metodo=" + metodo +
                ", status=" + status +
                ", referencia='" + referencia + '\'' +
                '}';
    }
}
