package com.hotel.gerenciador.model;

import java.time.LocalDateTime;

import com.hotel.gerenciador.util.MetodoPagamento;
import com.hotel.gerenciador.util.StatusPagamento;
import com.hotel.gerenciador.util.Validator;
import com.hotel.gerenciador.util.Formatter;

public class Pagamento {
    private int id;
    private double valor;
    private LocalDateTime dataPagamento;
    private MetodoPagamento metodo;
    private StatusPagamento status;
    private String referencia;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public Pagamento(int id, double valor, LocalDateTime dataPagamento, MetodoPagamento metodo,
                    StatusPagamento status, String referencia, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        Validator.validatePositiveValue(valor);
        Validator.validateNotFutureDateTime(dataPagamento);
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

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }
    public void setDataPagamento(LocalDateTime dataPagamento) {
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

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    @Override
    public String toString() {
        return "Pagamento{" +
                "id=" + id +
                ", valor=" + Formatter.formatCurrency(valor) +
                ", dataPagamento=" + Formatter.formatDateTime(dataPagamento) +
                ", metodo=" + metodo +
                ", status=" + status +
                ", referencia='" + referencia + '\'' +
                ", dataCriacao=" + Formatter.formatDateTime(dataCriacao) +
                ", dataAtualizacao=" + Formatter.formatDateTime(dataAtualizacao) +
                '}';
    }
}
