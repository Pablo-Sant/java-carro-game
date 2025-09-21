package com.example.controller;

import com.example.model.PlayerCar;
import com.example.view.PlayerView;
import com.example.view.RoadView;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public class GameManager {
    private Pane root;
    private PlayerCar playerModel;
    private PlayerView playerView;
    private RoadView roadView;
    
    public GameManager() {
        root = new Pane();
        initializeGame();
    }
    
    private void initializeGame() {
        // ✅ Cria o cenário da pista
        roadView = new RoadView();
        
        // ✅ Cria o carro do jogador
        playerModel = new PlayerCar(400, 500);
        playerView = new PlayerView();
        playerView.setPosition(playerModel.getX(), playerModel.getY());
        
        // ✅ Adiciona primeiro o cenário, depois o carro (ordem Z)
        root.getChildren().add(roadView.getRoot());
        root.getChildren().add(playerView.getView());
        
        setupInputHandlers();
    }
    
    private void setupInputHandlers() {
        root.setOnKeyPressed(event -> {
            // ✅ CORREÇÃO: Use switch tradicional com break
            KeyCode tecla = event.getCode();
            if (tecla == KeyCode.LEFT) {
                moverCarroEsquerda();
            } else if (tecla == KeyCode.RIGHT) {
                moverCarroDireita();
            }
        });
        root.setFocusTraversable(true);
        root.requestFocus();
    }
    
    // ✅ Alternativa com switch tradicional (se preferir)
    private void setupInputHandlersAlternative() {
        root.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    moverCarroEsquerda();
                    break;
                case RIGHT:
                    moverCarroDireita();
                    break;
                default:
                    break;
            }
        });
        root.setFocusTraversable(true);
        root.requestFocus();
    }
    
    public void moverCarroEsquerda() {
        playerModel.moverEsquerda();
        playerView.setPosition(playerModel.getX(), playerModel.getY());
    }
    
    public void moverCarroDireita() {
        playerModel.moverDireita();
        playerView.setPosition(playerModel.getX(), playerModel.getY());
    }
    
    public Pane getRoot() { return root; }
}