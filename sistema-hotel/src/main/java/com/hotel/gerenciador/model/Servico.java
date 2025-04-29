package com.hotel.gerenciador.model;

import java.time.LocalDateTime;

import com.hotel.gerenciador.util.Validator;

import com.hotel.gerenciador.util.Formatter;

public class Servico {
    private int id;
    private String nome;
    private String descricao;
    private double preco;
    private boolean disponivel;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public Servico(int id, String nome, String descricao, double preco, boolean disponivel,
                   LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        setId(id);
        setNome(nome);
        setDescricao(descricao);
        setPreco(preco);
        setDisponivel(disponivel);
        setDataCriacao(dataCriacao);
        setDataAtualizacao(dataAtualizacao);
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
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do serviço não pode ser nulo ou vazio.");
        }
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descrição do serviço não pode ser nula ou vazia.");
        }
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }
    public void setPreco(double preco) {
        if (preco < 0) {
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
        Validator.validateNotFutureDateTime(dataCriacao);
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        Validator.validateNotFutureDateTime(dataAtualizacao);
        this.dataAtualizacao = dataAtualizacao;
    }

    @Override
    public String toString() {
        return "Servico {\n" +
            "  id=" + id + ",\n" +
            "  nome='" + nome + "',\n" +
            "  descricao='" + descricao + "',\n" +
            "  preco=" + Formatter.formatCurrency(preco) + ",\n" +
            "  disponivel=" + disponivel + ",\n" +
            "  dataCriacao=" + Formatter.formatDateTime(dataCriacao) + ",\n" +
            "  dataAtualizacao=" + Formatter.formatDateTime(dataAtualizacao) + "\n" +
            '}';
    }
}
