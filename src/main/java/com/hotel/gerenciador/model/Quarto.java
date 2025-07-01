package com.hotel.gerenciador.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.hotel.gerenciador.util.StatusQuarto;
import com.hotel.gerenciador.util.TipoQuarto;
import com.hotel.gerenciador.util.Validator;
import com.hotel.gerenciador.util.Formatter;

public class Quarto {
    private int id;
    private int numeroQuarto;
    private TipoQuarto tipo;
    private BigDecimal precoDiaria;
    private StatusQuarto status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public Quarto() {}

    public Quarto(int id, int numeroQuarto, TipoQuarto tipo, BigDecimal precoDiaria,
                  StatusQuarto status, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.numeroQuarto = numeroQuarto;
        setTipo(tipo);
        setPrecoDiaria(precoDiaria);
        setStatus(status);
        this.numeroQuarto = numeroQuarto;

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
        Validator.validatePositiveId(numeroQuarto, "Número do quarto");
        this.numeroQuarto = numeroQuarto;
    }

    public TipoQuarto getTipo() {
        return tipo;
    }
    public void setTipo(TipoQuarto tipo) {
        Validator.validateEnum(tipo, "Tipo do quarto");
        this.tipo = tipo;
    }

    public BigDecimal getPrecoDiaria() {
        return precoDiaria;
    }
    public void setPrecoDiaria(BigDecimal precoDiaria) {
        Validator.validatePositiveValue(precoDiaria);
        this.precoDiaria = precoDiaria;
    }

    public StatusQuarto getStatus() {
        return status;
    }
    public void setStatus(StatusQuarto status) {
        Validator.validateEnum(status, "Status do quarto");
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    public void setDataCriacao(LocalDateTime dataCriacao) {
        if (dataCriacao != null) {
            Validator.validateNotFutureDateTime(dataCriacao);
        }
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        if (dataAtualizacao != null) {
            Validator.validateNotFutureDateTime(dataAtualizacao);
        }
        this.dataAtualizacao = dataAtualizacao;
    }

    public BigDecimal calcularPrecoTotal(int dias) {
        if (dias < 0) {
            throw new IllegalArgumentException("O número de dias não pode ser negativo.");
        }
        if (this.precoDiaria == null) {
            return BigDecimal.ZERO; 
        }
        return this.precoDiaria.multiply(new BigDecimal(dias));
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
                ", dataCriacao=" + (dataCriacao != null ? Formatter.formatDateTime(dataCriacao) : "null") +
                ", dataAtualizacao=" + (dataAtualizacao != null ? Formatter.formatDateTime(dataAtualizacao) : "null") +
                '}';
    }
}