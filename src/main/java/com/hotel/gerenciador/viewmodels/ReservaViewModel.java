package com.hotel.gerenciador.viewmodels;

import javafx.beans.property.*;

public class ReservaViewModel {
    private IntegerProperty id;
    private StringProperty cliente;
    private StringProperty quarto;
    private StringProperty dataEntrada;
    private StringProperty dataSaida;
    private StringProperty status;
    private StringProperty valorTotal;

    public ReservaViewModel(int id, String cliente, String quarto, String dataEntrada, String dataSaida, String status, StringProperty valorTotal) {
        this.id = new SimpleIntegerProperty(id);
        this.cliente = new SimpleStringProperty(cliente);
        this.quarto = new SimpleStringProperty(quarto);
        this.dataEntrada = new SimpleStringProperty(dataEntrada);
        this.dataSaida = new SimpleStringProperty(dataSaida);
        this.status = new SimpleStringProperty(status);
        this.valorTotal = valorTotal != null ? valorTotal : new SimpleStringProperty("0.00");
    }

    public IntegerProperty idProperty() { return id; }
    public StringProperty clienteProperty() { return cliente; }
    public StringProperty quartoProperty() { return quarto; }
    public StringProperty dataEntradaProperty() { return dataEntrada; }
    public StringProperty dataSaidaProperty() { return dataSaida; }
    public StringProperty statusProperty() { return status; }
    public StringProperty valorTotalProperty() { return valorTotal; }
}