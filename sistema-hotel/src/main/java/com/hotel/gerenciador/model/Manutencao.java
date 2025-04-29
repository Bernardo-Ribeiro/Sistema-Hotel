package com.hotel.gerenciador.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hotel.gerenciador.util.StatusManutencao;
import com.hotel.gerenciador.util.Formatter;

public class Manutencao {
    private int id;
    private Integer idQuarto;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String descricao;
    private StatusManutencao status;
    private Integer idFuncionario;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public Manutencao(Integer idQuarto, LocalDate dataInicio, LocalDate dataFim, String descricao, 
                      StatusManutencao status, Integer idFuncionario) {
        this.idQuarto = idQuarto;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.descricao = descricao;
        this.status = status;
        this.idFuncionario = idFuncionario;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Integer getIdQuarto() {
        return idQuarto;
    }
    public void setIdQuarto(Integer idQuarto) {
        this.idQuarto = idQuarto;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }
    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }
    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusManutencao getStatus() {
        return status;
    }
    public void setStatus(StatusManutencao status) {
        this.status = status;
    }

    public Integer getIdFuncionario() {
        return idFuncionario;
    }
    public void setIdFuncionario(Integer idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    @Override
    public String toString() {
        return "Manutencao{" +
                "id=" + id +
                ", idQuarto=" + idQuarto +
                ", dataInicio=" + Formatter.formatDate(dataInicio) +
                ", dataFim=" + Formatter.formatDate(dataFim) +
                ", descricao='" + descricao + '\'' +
                ", status=" + status +
                ", idFuncionario=" + idFuncionario +
                ", dataCriacao=" + Formatter.formatDateTime(dataCriacao) +
                ", dataAtualizacao=" + Formatter.formatDateTime(dataAtualizacao) +
                '}';
    }
}
