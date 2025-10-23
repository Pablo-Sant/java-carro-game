package com.example;

import com.example.controller.GameManager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        try {
            GameManager gameManager = new GameManager();
            Scene scene = new Scene(gameManager.getRoot(), 800, 600);

            
            scene.setOnMouseClicked(event -> {
                gameManager.getRoot().requestFocus();
            });
            
            stage.setTitle("Jogo de Carro - Desvie dos Obstáculos!");
            stage.setScene(scene);
            stage.show();

            gameManager.getRoot().requestFocus();
            
            System.out.println("Jogo iniciado com sucesso!");
            
        } catch (Exception e) {
            System.err.println("Erro ao iniciar o jogo:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}