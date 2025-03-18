package com.hotel.gerenciador.model;

public class Quarto{
    private int id;
    private String numeroQuarto;
    private String tipo;
    private double preco;
    private String status;
    private int idHospede;

    public Quarto(int id, String numeroQuarto, String tipo, double preco, String status, int idHospede){
        this.id = id;
        this.numeroQuarto = numeroQuarto;
        this.tipo = tipo;
        this.preco = preco;
        this.status = status;
        this.idHospede = idHospede;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNumeroQuarto() {
        return numeroQuarto;
    }
    public void setNumeroQuarto(String numeroQuarto) {
        this.numeroQuarto = numeroQuarto;
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPreco() {
        return preco;
    }
    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
    public int getIdHospede() {
        return idHospede;
    }
    public void setIdHospede(int idHospede) {
        this.idHospede = idHospede;
    }
}