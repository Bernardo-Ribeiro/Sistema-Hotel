package com.hotel.gerenciador.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class MainLayoutController {

    @FXML private StackPane contentArea;

    @FXML
    private void initialize() {
        abrirDashboard();
    }

    @FXML
    private void abrirDashboard() {
        loadView("/views/Dashboard.fxml");
    }

    @FXML
    private void abrirMapaQuartos() {
        loadView("/views/MapaQuartos.fxml");
    }

    @FXML
    private void abrirReservas() {
        loadView("/views/Reservas.fxml");
    }

    @FXML
    private void abrirCheckIn() {
        loadView("/views/CheckIn.fxml");
    }

    @FXML
    private void abrirGerenciamentoHospedes() {
        loadView("/views/GerenciamentoHospedes.fxml");
    }

    @FXML
    private void abrirFuncionarios() {
        loadView("/views/GerenciamentoFuncionario.fxml");
    }

    @FXML
    private void abrirManutencao() {
        loadView("/views/GerenciamentoManutencao.fxml");
    }

    @FXML
    private void sair() {
        System.exit(0);
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao Carregar Tela");
            alert.setContentText("Não foi possível carregar a tela: " + fxmlPath);
            alert.showAndWait();
        }
    }
} 