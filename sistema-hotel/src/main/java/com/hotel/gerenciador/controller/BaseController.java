package com.hotel.gerenciador.controller;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public abstract class BaseController {
    
    protected void mostrarAlerta(String titulo, String mensagem) {
        Alert.AlertType tipo = Alert.AlertType.INFORMATION;
        String tituloLower = titulo.toLowerCase();
        
        if (tituloLower.contains("erro") || tituloLower.contains("falha") || tituloLower.contains("conflito")) {
            tipo = Alert.AlertType.ERROR;
        } else if (tituloLower.contains("aviso") || tituloLower.contains("atenção") || 
                  tituloLower.contains("pendente") || tituloLower.contains("inválida")) {
            tipo = Alert.AlertType.WARNING;
        }
        
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    protected void fecharJanela(Stage stage) {
        if (stage != null) {
            stage.close();
        }
    }
} 