package com.hotel.gerenciador.model;

import java.time.LocalDateTime;

public class Manutencao {
    private int id;
    private Integer idQuarto;
    private Integer idFuncionario;
    private String descricao;
    private LocalDateTime dataSolicitacao;
    private LocalDateTime dataConclusao;
    private StatusManutencao status;

    public Manutencao(int id, Integer idQuarto, Integer idFuncionario, String descricao, LocalDateTime dataSolicitacao, LocalDateTime dataConclusao, StatusManutencao status) {
        this.id = id;
        this.idQuarto = idQuarto;
        this.idFuncionario = idFuncionario;
        this.descricao = descricao;
        this.dataSolicitacao = dataSolicitacao;
        this.dataConclusao = dataConclusao;
        this.status = status;
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

    public Integer getIdFuncionario() {
        return idFuncionario;
    }
    public void setIdFuncionario(Integer idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }
    public void setDataSolicitacao(LocalDateTime dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }
    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public StatusManutencao getStatus() {
        return status;
    }
    public void setStatus(StatusManutencao status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Manutencao{" +
                "id=" + id +
                ", idQuarto=" + idQuarto +
                ", idFuncionario=" + idFuncionario +
                ", descricao='" + descricao + '\'' +
                ", dataSolicitacao=" + dataSolicitacao +
                ", dataConclusao=" + dataConclusao +
                ", status=" + status +
                '}';
    }
}
