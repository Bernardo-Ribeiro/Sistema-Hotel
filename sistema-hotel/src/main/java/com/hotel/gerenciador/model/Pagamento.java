package com.hotel.gerenciador.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hotel.gerenciador.util.MetodoPagamento;
import com.hotel.gerenciador.util.StatusPagamento;
import com.hotel.gerenciador.util.Validator;
import com.hotel.gerenciador.util.Formatter;

public class Pagamento {
    private int id;
    private BigDecimal valor;
    private LocalDate dataPagamento;
    private MetodoPagamento metodo;
    private StatusPagamento status;
    private int reservaId;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public Pagamento() {}

    public Pagamento(int id, BigDecimal valor, LocalDate dataPagamento, MetodoPagamento metodo,
                   StatusPagamento status, int reservaId, 
                   LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser maior que zero.");
        }
        Validator.validateNotFutureDate(dataPagamento);

        this.valor = valor;
        this.dataPagamento = dataPagamento;
        this.metodo = metodo;
        this.status = status;
        this.reservaId = reservaId;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }
    public void setValor(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser maior que zero.");
        }
        this.valor = valor;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }
    public void setDataPagamento(LocalDate dataPagamento) {
        Validator.validateNotFutureDate(dataPagamento);
        this.dataPagamento = dataPagamento;
    }

    public MetodoPagamento getMetodo() {
        return metodo;
    }
    public void setMetodo(MetodoPagamento metodo) {
        Validator.validateEnum(metodo, "Método de pagamento");
        this.metodo = metodo;
    }

    public StatusPagamento getStatus() {
        return status;
    }
    public void setStatus(StatusPagamento status) {
        Validator.validateEnum(status, "Status do pagamento");
        this.status = status;
    }

    public int getReservaId() {
        return reservaId;
    }
    public void setReservaId(int reservaId) {
        Validator.validatePositiveId(reservaId, "ID da reserva");
        this.reservaId = reservaId;
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
                ", valor=" + (valor != null ? Formatter.formatCurrency(valor.doubleValue()) : "null") +
                ", dataPagamento=" + (dataPagamento != null ? Formatter.formatDate(dataPagamento) : "null") +
                ", metodo=" + metodo +
                ", status=" + status +
                ", reservaId=" + reservaId +
                ", dataCriacao=" + (dataCriacao != null ? Formatter.formatDateTime(dataCriacao) : "null") +
                ", dataAtualizacao=" + (dataAtualizacao != null ? Formatter.formatDateTime(dataAtualizacao) : "null") +
                '}';
    }
}