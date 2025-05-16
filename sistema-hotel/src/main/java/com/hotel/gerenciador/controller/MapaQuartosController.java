package com.hotel.gerenciador.controller;

import com.hotel.gerenciador.model.Quarto;
import com.hotel.gerenciador.model.Reserva;
import com.hotel.gerenciador.service.QuartoService;
import com.hotel.gerenciador.service.ReservaService;
import com.hotel.gerenciador.util.StatusQuarto;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import java.util.List;
import java.util.Map;

public class MapaQuartosController {

    @FXML
    private GridPane gridMapaQuartos;

    private QuartoService quartoService = new QuartoService();
    private ReservaService reservaService = new ReservaService();

    @FXML
    private void initialize() {
        List<Quarto> quartos = quartoService.findAll();
        Map<Integer, Reserva> reservasAtuais = reservaService.findReservasAtivasMapPorQuarto();

        int totalAndares = 3;
        int quartosPorAndar = 10;

        for (int andar = 0; andar < totalAndares; andar++) {
            for (int i = 0; i < quartosPorAndar; i++) {
                int index = andar * quartosPorAndar + i;
                if (index >= quartos.size()) break;
                Quarto quarto = quartos.get(index);

                Circle icone = new Circle(20);
                icone.setFill(getCorPorStatus(quarto.getStatus()));

                Reserva reserva = reservasAtuais.get(quarto.getId());

                StringBuilder tooltipBuilder = new StringBuilder("Quarto: " + quarto.getNumeroQuarto());
                if (reserva != null && reserva.getHospede() != null) {
                    tooltipBuilder.append("\nHóspede: ").append(reserva.getHospede().getNome());
                    tooltipBuilder.append("\nReserva Nº: ").append(reserva.getId());
                    tooltipBuilder.append("\nEntrada: ").append(reserva.getDataCheckIn());
                    tooltipBuilder.append("\nSaída: ").append(reserva.getDataCheckOut());
                } else {
                    tooltipBuilder.append("\nStatus: ").append(quarto.getStatus());
                }
                final String tooltipText = tooltipBuilder.toString();

                StackPane stack = new StackPane(icone);
                stack.setOnMouseEntered(e -> mostrarTooltip(e, tooltipText));
                stack.setOnMouseExited(e -> ocultarTooltip());

                gridMapaQuartos.add(stack, i, andar);
            }
        }
    }

    private Color getCorPorStatus(StatusQuarto status) {
        switch (status) {
            case DISPONIVEL:
                return Color.LIGHTGREEN;
            case OCUPADO:
                return Color.TOMATO;
            case MANUTENCAO:
                return Color.GOLD;
            default:
                return Color.GRAY;
        }
    }


    private final Popup tooltip = new Popup();

    private void mostrarTooltip(MouseEvent e, String texto) {
        Label label = new Label(texto);
        label.setStyle("-fx-background-color: white; -fx-padding: 5; -fx-border-color: black; -fx-border-width: 1;");
        tooltip.getContent().clear();
        tooltip.getContent().add(label);
        tooltip.show(((StackPane) e.getSource()).getScene().getWindow(), e.getScreenX() + 10, e.getScreenY() + 10);
    }

    private void ocultarTooltip() {
        tooltip.hide();
    }
} 
