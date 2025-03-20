package com.hotel.gerenciador.model;

public class Consumo {
    private int id;
    private int idHospede;
    private int idProduto;
    private double valor;
    private int quantidade;
    private String dataConsumo;

    public Consumo(int id, int idHospede, int idProduto, double valor, int quantidade, String dataConsumo) {
        this.id = id;
        this.idHospede = idHospede;
        this.idProduto = idProduto;
        this.valor = valor;
        this.quantidade = quantidade;
        this.dataConsumo = dataConsumo;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getIdHospede() {
        return idHospede;
    }
    public void setIdHospede(int idHospede) {
        this.idHospede = idHospede;
    }

    public int getIdProduto() {
        return idProduto;
    }
    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public double getValor() {
        return valor;
    }
    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getDataConsumo() {
        return dataConsumo;
    }
    public void setDataConsumo(String dataConsumo) {
        this.dataConsumo = dataConsumo;
    }

    @Override
    public String toString() {
        return "Consumo{" +
                "id=" + id +
                ", idHospede=" + idHospede +
                ", idProduto=" + idProduto +
                ", valor=" + valor +
                ", quantidade=" + quantidade +
                ", dataConsumo='" + dataConsumo + '\'' +
                '}';
    }
}
