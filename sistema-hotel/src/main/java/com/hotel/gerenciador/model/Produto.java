package com.hotel.gerenciador.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.hotel.gerenciador.util.CategoriaProduto;
import com.hotel.gerenciador.util.Formatter;
import com.hotel.gerenciador.util.Validator;

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
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do produto não pode ser vazio.");
        }
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        if (descricao != null && descricao.length() > 255) {
            throw new IllegalArgumentException("A descrição do produto não pode ter mais de 255 caracteres.");
        }
        this.descricao = descricao == null ? "" : descricao.trim();
    }

    public BigDecimal getPreco() {
        return preco;
    }
    public void setPreco(BigDecimal preco) {
        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser maior que zero.");
        }
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
        if (categoria == null) {
            throw new IllegalArgumentException("A categoria do produto não pode ser nula.");
        }
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