package com.hotel.gerenciador.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.hotel.gerenciador.utils.CategoriaProduto;
import com.hotel.gerenciador.utils.Formatter;
import com.hotel.gerenciador.utils.Validator;

public class Produto {
    private int id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private int estoque;
    private CategoriaProduto categoria;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public Produto() {}

    public Produto(int id, String nome, String descricao, BigDecimal preco, int estoque,
                   CategoriaProduto categoria, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        setNome(nome);
        setDescricao(descricao);
        setPreco(preco);
        setEstoque(estoque);
        setCategoria(categoria);
        setDataCriacao(dataCriacao);
        setDataAtualizacao(dataAtualizacao);
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        Validator.validatePositiveId(id, "ID do produto");
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        Validator.validateNotEmpty(nome, "Nome do produto");
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
        Validator.validatePositiveValue(preco);
        this.preco = preco;
    }

    public int getEstoque() {
        return estoque;
    }
    public void setEstoque(int estoque) {
        if (estoque < 0) {
            throw new IllegalArgumentException("O estoque não pode ser negativo.");
        }
        this.estoque = estoque;
    }

    public CategoriaProduto getCategoria() {
        return categoria;
    }
    public void setCategoria(CategoriaProduto categoria) {
        Validator.validateEnum(categoria, "Categoria do produto");
        this.categoria = categoria;
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
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", preco=" + (preco != null ? Formatter.formatCurrency(preco.doubleValue()) : "null") +
                ", estoque=" + estoque +
                ", categoria=" + categoria +
                ", dataCriacao=" + (dataCriacao != null ? Formatter.formatDateTime(dataCriacao) : "null") +
                ", dataAtualizacao=" + (dataAtualizacao != null ? Formatter.formatDateTime(dataAtualizacao) : "null") +
                '}';
    }
}