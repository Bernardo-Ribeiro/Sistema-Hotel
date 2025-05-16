package com.hotel.gerenciador;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            URL url = getClass().getResource("/view/Dashboard.fxml");
            System.out.println("FXML URL: " + url);
            
            if (url == null) {
                System.err.println("Arquivo FXML não encontrado! Verifique o caminho e a estrutura do projeto.");
                System.exit(1);
            }
            
            Parent root = FXMLLoader.load(url);
            Scene scene = new Scene(root);
            primaryStage.setTitle("Sistema de Gerenciamento de Hotel");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.setMaximized(true);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
