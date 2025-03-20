package com.hotel.gerenciador.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Consumo {
    private int id;
    private int idHospede;
    private int idProduto;
    private double valor;
    private int quantidade;
    private LocalDate dataConsumo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public Consumo(int id, int idHospede, int idProduto, double valor, int quantidade,
                    LocalDate dataConsumo, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.idHospede = idHospede;
        this.idProduto = idProduto;
        this.valor = valor;
        this.quantidade = quantidade;
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

    public int getIdHospede() {
        return idHospede;
    }
    public void setIdHospede(int idHospede) {
        this.idHospede = idHospede;
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
        this.valor = valor;
    }

    public int getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
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
        return "Consumo{" +
                "id=" + id +
                ", idHospede=" + idHospede +
                ", idProduto=" + idProduto +
                ", valor=" + valor +
                ", quantidade=" + quantidade +
                ", dataConsumo='" + dataConsumo + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", dataAtualizacao=" + dataAtualizacao +
                '}';
    }
}
