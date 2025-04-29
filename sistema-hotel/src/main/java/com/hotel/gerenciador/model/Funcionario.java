package com.hotel.gerenciador.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hotel.gerenciador.util.Formatter;
import com.hotel.gerenciador.util.Validator;

public class Funcionario {

    private int id;
    private String nome;
    private String cargo;
    private double salario;
    private String telefone;
    private String cpf;
    private String email;
    private String endereco;
    private LocalDate dataAdmissao;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public Funcionario(int id, String nome, String cargo, double salario, String telefone,
                       String cpf, String email, String endereco, LocalDate dataAdmissao,
                       LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.nome = nome;
        this.cargo = cargo;
        setSalario(salario);
        setTelefone(telefone);
        setCpf(cpf);
        setEmail(email);
        this.endereco = endereco;
        setDataAdmissao(dataAdmissao);
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
        this.nome = nome;
    }

    public String getCargo() {
        return cargo;
    }
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public double getSalario() {
        return salario;
    }
    public void setSalario(double salario) {
        Validator.validatePositiveValue(salario);
        this.salario = salario;
    }

    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        Validator.validateTelefone(telefone);
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        Validator.validateCpf(cpf);
        this.cpf = cpf;
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
        this.endereco = endereco;
    }

    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }
    public void setDataAdmissao(LocalDate dataAdmissao) {
        Validator.validateNotFutureDate(dataAdmissao);
        this.dataAdmissao = dataAdmissao;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    @Override
    public String toString() {
        return "Funcionario {\n" +
               "  id=" + id + ",\n" +
               "  nome='" + nome + "',\n" +
               "  cargo='" + cargo + "',\n" +
               "  salario=" + Formatter.formatCurrency(salario) + ",\n" +
               "  telefone='" + Formatter.formatPhone(telefone) + "',\n" +
               "  cpf='" + Formatter.formatCpf(cpf) + "',\n" +
               "  email='" + Formatter.formatEmail(email) + "',\n" +
               "  endereco='" + Validator.validateAndFormatEndereco(endereco) + "',\n" +
               "  dataAdmissao=" + Formatter.formatDate(dataAdmissao) + ",\n" +
               "  dataCriacao=" + Formatter.formatDateTime(dataCriacao) + ",\n" +
               "  dataAtualizacao=" + Formatter.formatDateTime(dataAtualizacao) + "\n" +
               '}';
    }
}
