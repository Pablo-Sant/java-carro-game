package com.example.controller;

import java.util.List;

import com.example.model.PlayerCar;
import com.example.view.PlayerView;
import com.example.view.RoadView;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class GameManager {
    private Pane root;
    private PlayerCar playerModel;
    private PlayerView playerView;
    private RoadView roadView;
    private AnimationTimer gameLoop;
    private double velocidadeCenario = 3;
    
    public GameManager() {
        root = new Pane();
        initializeGame();
        setupGameLoop();
    }
    
    private void initializeGame() {
        roadView = new RoadView();
        playerModel = new PlayerCar(400, 500);
        playerView = new PlayerView();
        playerView.setPosition(playerModel.getX(), playerModel.getY());
        
        root.getChildren().add(roadView.getRoot());
        root.getChildren().add(playerView.getView());
        
        // ✅ CONFIGURAÇÃO CORRETA DO FOCO E INPUT
        configurarControles();
    }
    
    private void configurarControles() {
        // ✅ 1. Configura os handlers de teclado
        root.setOnKeyPressed(event -> {
            System.out.println("Tecla pressionada: " + event.getCode());
            
            if (event.getCode() == KeyCode.LEFT) {
                System.out.println("Movendo para ESQUERDA");
                moverCarroEsquerda();
            } else if (event.getCode() == KeyCode.RIGHT) {
                System.out.println("Movendo para DIREITA");
                moverCarroDireita();
            }
        });
        
        // ✅ 2. Garante que o root é focalizável
        root.setFocusTraversable(true);
        
        // ✅ 3. Solicita foco IMEDIATAMENTE
        root.requestFocus();
        
        System.out.println("Controles configurados! Pressione ← ou →");
    }
    
    private void setupGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                animarCenario();
            }
        };
        gameLoop.start();
    }
    
    private void animarCenario() {
        List<Rectangle> faixas = roadView.getFaixas();
        for (Rectangle faixa : faixas) {
            faixa.setLayoutY(faixa.getLayoutY() + velocidadeCenario);
            if (faixa.getLayoutY() > 600) {
                faixa.setLayoutY(-30);
            }
        }
    }
    
    public void moverCarroEsquerda() {
        playerModel.moverEsquerda();
        playerView.setPosition(playerModel.getX(), playerModel.getY());
        System.out.println("Posição X do carro: " + playerModel.getX());
    }
    
    public void moverCarroDireita() {
        playerModel.moverDireita();
        playerView.setPosition(playerModel.getX(), playerModel.getY());
        System.out.println("Posição X do carro: " + playerModel.getX());
    }
    
    public Pane getRoot() { 
        // ✅ Garante que o root sempre tenha foco quando retornado
        root.requestFocus();
        return root; 
    }
}