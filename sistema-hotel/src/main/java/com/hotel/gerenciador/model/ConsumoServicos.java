package com.hotel.gerenciador.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hotel.gerenciador.util.Formatter;
import com.hotel.gerenciador.util.Validator;

public class ConsumoServicos {
    private int id;
    private int reservaId;
    private int servicoId;
    private int quantidade;
    private LocalDate dataConsumo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public ConsumoServicos(int id, int reservaId, int servicoId, int quantidade,
                LocalDate dataConsumo, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.reservaId = reservaId;
        this.servicoId = servicoId;
        setQuantidade(quantidade);
        this.dataConsumo = dataConsumo;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    public ConsumoServicos() {}

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
        Validator.validatePositiveId(reservaId, "ID da reserva");
        this.reservaId = reservaId;
    }

    public int getServicoId() {
        return servicoId;
    }

    public void setServicoId(int servicoId) {
        Validator.validatePositiveId(servicoId, "ID do serviço");
        this.servicoId = servicoId;
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
        Validator.validateNotNull(dataConsumo, "Data de consumo");
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
        return "ConsumoServicos{" +
                "id=" + id +
                ", reservaId=" + reservaId +
                ", servicoId=" + servicoId +
                ", quantidade=" + quantidade +
                ", dataConsumo=" + Formatter.formatDate(dataConsumo) +
                ", dataCriacao=" + Formatter.formatDateTime(dataCriacao) +
                ", dataAtualizacao=" + Formatter.formatDateTime(dataAtualizacao) +
                '}';
    }

}
