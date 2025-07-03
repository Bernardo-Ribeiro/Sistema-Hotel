package com.hotel.gerenciador.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.time.LocalDate;

public class CheckInController extends BaseController {
    @FXML private DatePicker dateFiltroReferencia;
    @FXML private TextField txtBuscaGeral;
    @FXML private Button btnBuscaGeral;
    @FXML private TabPane tabPanePrincipal;
    @FXML private Tab tabCheckIn;
    @FXML private Tab tabCheckOut;
    @FXML private VBox checkInContent;
    @FXML private VBox checkOutContent;

    private CheckInTabController checkInTabController;
    private CheckOutTabController checkOutTabController;

    @FXML
    private void initialize() {
        try {
            // Carregar Check-In Tab
            FXMLLoader checkInLoader = new FXMLLoader(getClass().getResource("/views/CheckInTab.fxml"));
            checkInContent.getChildren().add(checkInLoader.load());
            checkInTabController = checkInLoader.getController();

            // Carregar Check-Out Tab
            FXMLLoader checkOutLoader = new FXMLLoader(getClass().getResource("/views/CheckOutTab.fxml"));
            checkOutContent.getChildren().add(checkOutLoader.load());
            checkOutTabController = checkOutLoader.getController();

            // Configurar filtros e eventos
            configurarFiltrosEEventos();
            
            // Inicializar com a data atual
            dateFiltroReferencia.setValue(LocalDate.now());
            atualizarTodasAsListas();
            
        } catch (IOException e) {
            mostrarAlerta("Erro", "Não foi possível carregar as telas de check-in/check-out: " + e.getMessage());
        }
    }

    private void configurarFiltrosEEventos() {
        dateFiltroReferencia.setOnAction(event -> atualizarTodasAsListas());
        btnBuscaGeral.setOnAction(event -> handleBuscarGeral());
        txtBuscaGeral.setOnAction(event -> handleBuscarGeral());
    }

    @FXML
    private void handleBuscarGeral() {
        atualizarTodasAsListas();
    }

    private void atualizarTodasAsListas() {
        LocalDate data = dateFiltroReferencia.getValue();
        String termoBusca = txtBuscaGeral.getText();
        
        if (checkInTabController != null) {
            checkInTabController.carregarChegadas(data, termoBusca);
        }
        
        if (checkOutTabController != null) {
            checkOutTabController.atualizarListaHospedesSaidas(data, termoBusca);
        }
    }
}