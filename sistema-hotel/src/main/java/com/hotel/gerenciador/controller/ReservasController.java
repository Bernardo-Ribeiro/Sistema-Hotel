package com.hotel.gerenciador.controller;

import com.hotel.gerenciador.model.Reserva;
import com.hotel.gerenciador.viewmodel.ReservaViewModel;
import com.hotel.gerenciador.service.ReservaService;
import com.hotel.gerenciador.util.StatusReserva;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ReservasController {

    @FXML private TableView<ReservaViewModel> tabelaReservas;
    @FXML private TableColumn<ReservaViewModel, Integer> colId;
    @FXML private TableColumn<ReservaViewModel, String> colCliente;
    @FXML private TableColumn<ReservaViewModel, String> colQuarto;
    @FXML private TableColumn<ReservaViewModel, String> colDataEntrada;
    @FXML private TableColumn<ReservaViewModel, String> colDataSaida;
    @FXML private TableColumn<ReservaViewModel, String> colStatus;

    private ReservaService reservaService = new ReservaService();

    private ReservaViewModel toViewModel(Reserva reserva) {
        return new ReservaViewModel(
            reserva.getId(),
            reserva.getHospede().getNome(),
            String.valueOf(reserva.getQuarto().getNumeroQuarto()),
            reserva.getDataCheckIn().toString(),
            reserva.getDataCheckOut().toString(),
            reserva.getStatus().name()
        );
    }

    @FXML
    private void initialize() {
        colId.setCellValueFactory(cell -> cell.getValue().idProperty().asObject());
        colCliente.setCellValueFactory(cell -> cell.getValue().clienteProperty());
        colQuarto.setCellValueFactory(cell -> cell.getValue().quartoProperty());
        colDataEntrada.setCellValueFactory(cell -> cell.getValue().dataEntradaProperty());
        colDataSaida.setCellValueFactory(cell -> cell.getValue().dataSaidaProperty());
        colStatus.setCellValueFactory(cell -> cell.getValue().statusProperty());

        ObservableList<ReservaViewModel> reservas = FXCollections.observableArrayList();

        List<Reserva> listaReservas = reservaService.findReservasPorStatus(StatusReserva.CONFIRMADA);
        if (listaReservas != null) {
            for (Reserva r : listaReservas) {
                System.out.println("Hospede: " + (r.getHospede() != null ? r.getHospede().getNome() : "null"));
                System.out.println("Quarto: " + (r.getQuarto() != null ? r.getQuarto().getNumeroQuarto() : "null"));
                reservas.add(toViewModel(r));
            }

        }

        tabelaReservas.setItems(reservas);
    }

    @FXML
    private void novaReserva() {
        System.out.println("Criar nova reserva...");
    }

    @FXML
    private void editarReserva() {
        System.out.println("Editar reserva selecionada...");
    }

    @FXML
    private void cancelarReserva() {
        System.out.println("Cancelar reserva selecionada...");

    }
}
