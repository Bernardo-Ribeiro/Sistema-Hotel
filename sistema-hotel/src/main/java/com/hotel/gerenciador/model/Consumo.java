package com.hotel.gerenciador.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hotel.gerenciador.util.Formatter;
import com.hotel.gerenciador.util.Validator;

public class Consumo {
    private int id;
    private int idReserva;
    private int idProduto;
    private double valor;
    private int quantidade;
    private LocalDate dataConsumo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public Consumo(int id, int idReserva, int idProduto, double valor, int quantidade,
                   LocalDate dataConsumo, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.idReserva = idReserva;
        this.idProduto = idProduto;
        setValor(valor);
        setQuantidade(quantidade);
        setDataConsumo(dataConsumo);
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getIdReserva() {
        return idReserva;
    }
    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public int getIdProduto() {
        return idProduto;
    }
    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public double getValor() {
        return valor;
    }
    public void setValor(double valor) {
        Validator.validatePositiveValue(valor);
        this.valor = valor;
    }

    public int getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(int quantidade) {
        Validator.validatePositiveValue(quantidade);
        this.quantidade = quantidade;
    }

    public LocalDate getDataConsumo() {
        return dataConsumo;
    }
    public void setDataConsumo(LocalDate dataConsumo) {
        Validator.validateNotFutureDate(dataConsumo);
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
        return "Consumo{" +
                "id=" + id +
                ", idReserva=" + idReserva +
                ", idProduto=" + idProduto +
                ", valor=" + Formatter.formatCurrency(valor) +
                ", quantidade=" + quantidade +
                ", dataConsumo=" + Formatter.formatDate(dataConsumo) +
                ", dataCriacao=" + Formatter.formatDateTime(dataCriacao) +
                ", dataAtualizacao=" + Formatter.formatDateTime(dataAtualizacao) +
                '}';
    }
}
