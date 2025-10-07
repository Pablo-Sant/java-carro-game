package com.example.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.model.Obstacle;
import com.example.model.ObstacleFactory;
import com.example.model.PlayerCar;
import com.example.view.ObstacleView;
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
    private List<Obstacle> obstaculos;
    private List<ObstacleView> obstacleViews;
    private Random random;
    private long ultimoObstaculoTime = 0;
    private int pontuacao;


    
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

        obstaculos = new ArrayList<>();
        obstacleViews = new ArrayList<>();
        random = new Random();

        
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
                //System.out.println("Movendo para ESQUERDA");
                moverCarroEsquerda();
            } else if (event.getCode() == KeyCode.RIGHT) {
                //System.out.println("Movendo para DIREITA");
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
                atualizarObstaculos();
                gerarObstaculos(now);
                verificarColisoes();
                atualizarPontuacao();
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

        private void gerarObstaculos(long now) {
        // Gera obstáculos a cada 2 segundos
        if (now - ultimoObstaculoTime > 1_000_000_000L) {
            if (random.nextDouble() < 0.6) { // 60% de chance
                criarNovoObstaculo();
            }
            ultimoObstaculoTime = now;
        }
    }

    private void criarNovoObstaculo() {
        Obstacle obstaculo = ObstacleFactory.criarObstaculoAleatorio(velocidadeCenario);
        ObstacleView obstacleView = new ObstacleView(obstaculo);
        
        obstaculos.add(obstaculo);
        obstacleViews.add(obstacleView);
        root.getChildren().add(obstacleView.getView());
        
        System.out.println("Novo obstáculo: " + obstaculo.getTipo());
    }

    private void atualizarObstaculos() {
        for (int i = obstaculos.size() - 1; i >= 0; i--) {
            Obstacle obstaculo = obstaculos.get(i);
            ObstacleView obstacleView = obstacleViews.get(i);
            
            obstaculo.update();
            obstacleView.updatePosition(obstaculo.getX(), obstaculo.getY());
            
            if (obstaculo.isForaDaTela()) {
                // Remove obstáculo que saiu da tela
                root.getChildren().remove(obstacleView.getView());
                obstaculos.remove(i);
                obstacleViews.remove(i);
                pontuacao += 10; // Pontua por evitar obstáculo
                System.out.println("Pontuação: " + pontuacao);
            }
        }
    }

        private void verificarColisoes() {
        for (Obstacle obstaculo : obstaculos) {
            if (colidiuComPlayer(obstaculo)) {
                gameOver();
                break;
            }
        }
    }

    private boolean colidiuComPlayer(Obstacle obstaculo) {
        // Colisão simples retângulo x retângulo
        double playerX = playerModel.getX();
        double playerY = playerModel.getY();
        double playerWidth = 40; // Largura do carro
        double playerHeight = 70; // Altura do carro
        
        return playerX < obstaculo.getX() + obstaculo.getWidth() &&
               playerX + playerWidth > obstaculo.getX() &&
               playerY < obstaculo.getY() + obstaculo.getHeight() &&
               playerY + playerHeight > obstaculo.getY();
    }

        private void atualizarPontuacao() {
        // RF08: Aumenta velocidade progressivamente
        if (pontuacao > 0 && pontuacao % 100 == 0) {
            velocidadeCenario += 0.5;
            System.out.println("Velocidade aumentada para: " + velocidadeCenario);
        }
    }

    private void gameOver() {
        gameLoop.stop();
        System.out.println("🎮 GAME OVER! Pontuação final: " + pontuacao);
        // Aqui você pode adicionar uma tela de game over depois
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