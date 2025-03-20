package com.hotel.gerenciador.model;

import java.time.LocalDate;
import java.time.Period;

public class Hospede {
    private int id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private LocalDate dataNascimento;
    private String endereco;
    private String veiculo;

    public Hospede(int id, String nome, String cpf, String telefone, String email, LocalDate dataNascimento, String endereco, String veiculo) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.endereco = endereco;
        this.veiculo = (veiculo == null || veiculo.isEmpty()) ? "Nenhum" : veiculo;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;}

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEndereco() {
        return endereco;
    }
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getVeiculo() {
        return veiculo;
    }
    public void setVeiculo(String veiculo) { 
        this.veiculo = (veiculo == null || veiculo.isEmpty()) ? "Nenhum" : veiculo; 
    }

    public int calcularIdade() {
        return Period.between(this.dataNascimento, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return "Hospede{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", telefone='" + telefone + '\'' +
                ", email='" + email + '\'' +
                ", dataNascimento=" + dataNascimento +
                ", idade=" + calcularIdade() + " anos" +
                ", endereco='" + endereco + '\'' +
                ", veiculo='" + veiculo + '\'' +
                '}';
    }
}
