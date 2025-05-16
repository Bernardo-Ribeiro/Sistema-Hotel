package com.hotel.gerenciador.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class DashboardController {

    @FXML
    private StackPane mainContent;

    @FXML
    private Button btnReservas;

    @FXML
    private Button btnCheckIn;

    @FXML
    private Button btnFuncionarios;

    @FXML
    private Button btnManutencao;

    @FXML
    private Button btnRelatorios;

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnSair;

    @FXML
    private void initialize() {
        loadView("/view/Inicio.fxml");
    }

    @FXML
    private void abrirDashboard() {
        loadView("/view/Inicio.fxml");
    }

    @FXML
    private void abrirMapaQuartos() {
        loadView("/view/MapaQuartos.fxml");
    }

    @FXML
    private void abrirReservas() {
        System.out.println("clicou");
        loadView("/view/Reservas.fxml");
    }

    @FXML
    private void abrirCheckIn() {
        loadView("/view/CheckIn.fxml");
    }

    @FXML
    private void abrirFuncionarios() {
        loadView("/view/Funcionarios.fxml");
    }

    @FXML
    private void abrirManutencao() {
        loadView("/view/Manutencao.fxml");
    }

    @FXML
    private void abrirRelatorios() {
        loadView("/view/Relatorios.fxml");
    }

    @FXML
    private void sair() {
        System.out.println("Encerrando sessão...");
        System.exit(0);
    }

    private void loadView(String fxmlPath) {    
        System.out.println("Carregando view: " + fxmlPath);
        try {
            Node view = FXMLLoader.load(getClass().getResource(fxmlPath));
            mainContent.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
