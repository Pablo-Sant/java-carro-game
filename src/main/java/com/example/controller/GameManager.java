package com.example.controller;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameManager {
    private Pane root;
    
    public GameManager() {
        root = new Pane();
        initializeGame();
    }
    
    private void initializeGame() {
        // Cenário básico
        root.setStyle("-fx-background-color: #2b2b2b;");
        
        // Carro simples para testar
        Rectangle carro = new Rectangle(40, 70, Color.RED);
        carro.setLayoutX(400);
        carro.setLayoutY(500);
        
        root.getChildren().add(carro);
        
        System.out.println("GameManager inicializado!");
    }
    
    public Pane getRoot() {
        return root;
    }
}