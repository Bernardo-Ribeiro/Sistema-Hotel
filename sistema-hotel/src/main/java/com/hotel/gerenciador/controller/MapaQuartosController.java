package com.hotel.gerenciador.controller;

import com.hotel.gerenciador.model.Quarto;
import com.hotel.gerenciador.model.Reserva;
import com.hotel.gerenciador.service.QuartoService;
import com.hotel.gerenciador.service.ReservaService;
import com.hotel.gerenciador.util.Formatter;
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

    private final Popup tooltip = new Popup();

    @FXML
    private void initialize() {
        List<Quarto> quartos = quartoService.findAll();
        if (quartos == null || quartos.isEmpty()) {
            gridMapaQuartos.add(new Label("Nenhum quarto cadastrado."), 0, 0);
            return;
        }

        Map<Integer, Reserva> reservasAtuais = reservaService.findReservasAtivasMapPorQuarto();

        int quartosPorAndar = 10;
        int totalAndares = (int) Math.ceil((double) quartos.size() / quartosPorAndar);
        if (totalAndares == 0 && !quartos.isEmpty()) {
            totalAndares = 1;
        }


        for (int andar = 0; andar < totalAndares; andar++) {
            for (int i = 0; i < quartosPorAndar; i++) {
                int index = andar * quartosPorAndar + i;
                if (index >= quartos.size()) {
                    break; 
                }
                Quarto quarto = quartos.get(index);

                Circle icone = new Circle(20);
                icone.setFill(getCorPorStatus(quarto.getStatus()));
                icone.setStroke(Color.DIMGRAY);
                icone.setStrokeWidth(1);

                Label numeroQuartoLabel = new Label(String.valueOf(quarto.getNumeroQuarto()));
                numeroQuartoLabel.setTextFill(Color.BLACK);
                numeroQuartoLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");


                Reserva reserva = reservasAtuais.get(quarto.getId());

                StringBuilder tooltipBuilder = new StringBuilder("Quarto: " + quarto.getNumeroQuarto());
                tooltipBuilder.append("\nTipo: ").append(quarto.getTipo());
                tooltipBuilder.append("\nDiária: ").append(Formatter.formatCurrency(quarto.getPrecoDiaria()));

                if (reserva != null) {
                    tooltipBuilder.append("\nStatus: OCUPADO");
                    if (reserva.getHospede() != null) {
                        tooltipBuilder.append("\nHóspede: ").append(reserva.getHospede().getNome());
                    }
                    tooltipBuilder.append("\nReserva Nº: ").append(reserva.getId());
                    if (reserva.getDataCheckIn() != null) {
                        tooltipBuilder.append("\nEntrada: ").append(Formatter.formatDate(reserva.getDataCheckIn()));
                    }
                    if (reserva.getDataCheckOut() != null) {
                        tooltipBuilder.append("\nSaída: ").append(Formatter.formatDate(reserva.getDataCheckOut()));
                    }
                } else {
                    tooltipBuilder.append("\nStatus: ").append(quarto.getStatus());
                }
                final String tooltipText = tooltipBuilder.toString();

                StackPane stack = new StackPane();
                stack.getChildren().addAll(icone, numeroQuartoLabel);
                
                stack.setStyle("-fx-padding: 5;"); 


                stack.setOnMouseEntered(e -> mostrarTooltip(e, tooltipText, stack));
                stack.setOnMouseExited(e -> ocultarTooltip());
                
                stack.setOnMouseClicked(event -> handleQuartoClicked(quarto, reserva));


                gridMapaQuartos.add(stack, i, andar);
            }
        }
    }

    private Color getCorPorStatus(StatusQuarto status) {
        if (status == null) return Color.GRAY;
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

    private void mostrarTooltip(MouseEvent e, String texto, StackPane NoOrigem) {
        Label label = new Label(texto);
        label.setStyle("-fx-background-color: white; -fx-padding: 8px; -fx-border-color: #cccccc; -fx-border-width: 1px; -fx-border-radius: 4px; -fx-background-radius: 4px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 1);");
        tooltip.getContent().clear();
        tooltip.getContent().add(label);

        javafx.geometry.Point2D p = NoOrigem.localToScreen(NoOrigem.getBoundsInLocal().getMaxX(), NoOrigem.getBoundsInLocal().getMinY());
        tooltip.show(NoOrigem.getScene().getWindow(), p.getX() + 5, p.getY());
    }

    private void ocultarTooltip() {
        tooltip.hide();
    }

    private void handleQuartoClicked(Quarto quarto, Reserva reserva) {
        System.out.println("Quarto clicado: " + quarto.getNumeroQuarto());
        if (reserva != null) {
            System.out.println("Reserva associada: " + reserva.getId());
            // Exemplo: Abrir tela de detalhes da reserva
            // loadReservaDetailsView(reserva.getId());
        } else if (quarto.getStatus() == StatusQuarto.DISPONIVEL) {
            System.out.println("Quarto disponível. Iniciar nova reserva?");
            // Exemplo: Abrir tela para criar nova reserva para este quarto
            // loadNewReservaView(quarto.getId());
        } else if (quarto.getStatus() == StatusQuarto.MANUTENCAO) {
            System.out.println("Quarto em manutenção.");
            // loadManutencaoDetailsView(quarto.getId());
        }
    }
}