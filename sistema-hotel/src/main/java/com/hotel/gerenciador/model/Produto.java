package com.hotel.gerenciador.model;

import java.time.LocalDateTime;

import com.hotel.gerenciador.util.CategoriaProduto;
import com.hotel.gerenciador.util.Formatter;
import com.hotel.gerenciador.util.Validator;

public class Produto {
    private int id;
    private String nome;
    private String descricao;
    private double preco;
    private CategoriaProduto categoria;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public Produto(int id, String nome, String descricao, double preco, CategoriaProduto categoria,
                   LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        setNome(nome);
        setDescricao(descricao);
        setPreco(preco);
        setCategoria(categoria);
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
            throw new IllegalArgumentException("O nome do produto não pode ser vazio.");
        }
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao == null ? "" : descricao;
    }

    public double getPreco() {
        return preco;
    }
    public void setPreco(double preco) {
        Validator.validatePositiveValue(preco);
        this.preco = preco;
    }

    public CategoriaProduto getCategoria() {
        return categoria;
    }
    public void setCategoria(CategoriaProduto categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("A categoria do produto não pode ser nula.");
        }
        this.categoria = categoria;
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
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", preco=" + Formatter.formatCurrency(preco) +
                ", categoria=" + categoria +
                ", dataCriacao=" + Formatter.formatDateTime(dataCriacao) +
                ", dataAtualizacao=" + Formatter.formatDateTime(dataAtualizacao) +
                '}';
    }
}
