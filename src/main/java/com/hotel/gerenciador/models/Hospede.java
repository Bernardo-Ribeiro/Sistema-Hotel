package com.hotel.gerenciador.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hotel.gerenciador.utils.Formatter;
import com.hotel.gerenciador.utils.Validator;

public class Hospede {
    private int id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private String endereco;
    private LocalDate dataNascimento;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public Hospede() {}

    public Hospede(int id, String nome, String cpf, String telefone, String email,
                   String endereco, LocalDate dataNascimento,
                   LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
        this.dataNascimento = dataNascimento;
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
        Validator.validateNotEmpty(nome, "Nome do hóspede");
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        Validator.validateCpf(cpf);
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        Validator.validateTelefone(telefone);
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        Validator.validateEmail(email);
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }
    public void setEndereco(String endereco) {
        Validator.validateEndereco(endereco);
        this.endereco = endereco;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
    public void setDataNascimento(LocalDate dataNascimento) {
        Validator.validateNotNull(dataNascimento, "Data de nascimento");
        Validator.validateNotFutureDate(dataNascimento);
        this.dataNascimento = dataNascimento;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    @Override
    public String toString() {
        return "Hospede{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cpf='" + Formatter.formatCpf(cpf) + '\'' +
                ", telefone='" + Formatter.formatPhone(telefone) + '\'' +
                ", email='" + Formatter.formatEmail(email) + '\'' +
                ", endereco='" + endereco + '\'' +
                ", dataNascimento=" + Formatter.formatDate(dataNascimento) +
                ", dataCriacao=" + Formatter.formatDateTime(dataCriacao) +
                ", dataAtualizacao=" + Formatter.formatDateTime(dataAtualizacao) +
                '}';
    }
}