package com.hotel.gerenciador.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.hotel.gerenciador.util.Validator;
import com.hotel.gerenciador.util.Formatter;

public class Servico {
    private int id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private boolean disponivel;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public Servico() {}

    public Servico(int id, String nome, String descricao, BigDecimal preco, boolean disponivel,
                   LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        setNome(nome);
        setDescricao(descricao);
        setPreco(preco);
        setDisponivel(disponivel);
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        Validator.validateNotEmpty(nome, "Nome do serviço");
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        Validator.validateDescription(descricao, 255);
        this.descricao = descricao == null ? "" : descricao.trim();
    }

    public BigDecimal getPreco() {
        return preco;
    }
    public void setPreco(BigDecimal preco) {
        if (preco == null || preco.compareTo(BigDecimal.ZERO) < 0) {
             throw new IllegalArgumentException("Preço do serviço não pode ser negativo.");
        }

        this.preco = preco;
    }

    public boolean isDisponivel() {
        return disponivel;
    }
    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
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

    @Override
    public String toString() {
        return "Servico {\n" +
            "  id=" + id + ",\n" +
            "  nome='" + nome + "',\n" +
            "  descricao='" + descricao + "',\n" +
            "  preco=" + (preco != null ? Formatter.formatCurrency(preco.doubleValue()) : "null") + ",\n" +
            "  disponivel=" + disponivel + ",\n" +
            "  dataCriacao=" + (dataCriacao != null ? Formatter.formatDateTime(dataCriacao) : "null") + ",\n" +
            "  dataAtualizacao=" + (dataAtualizacao != null ? Formatter.formatDateTime(dataAtualizacao) : "null") + "\n" +
            '}';
    }
}