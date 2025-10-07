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
    private int vidas = 5; // ✅ COMEÇA COM 5 VIDAS
    private boolean jogoAtivo = true; // ✅ CONTROLE DE ESTADO DO JOGO
    
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
        
        configurarControles();
        System.out.println("🎮 Jogo iniciado! Vidas: " + vidas);
    }
    
    private void configurarControles() {
        root.setOnKeyPressed(event -> {
            if (!jogoAtivo) return; // ✅ Só move se o jogo estiver ativo
            
            if (event.getCode() == KeyCode.LEFT) {
                moverCarroEsquerda();
            } else if (event.getCode() == KeyCode.RIGHT) {
                moverCarroDireita();
            }
        });
        
        root.setFocusTraversable(true);
        root.requestFocus();
    }
    
    private void setupGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (jogoAtivo) { // ✅ Só atualiza se o jogo estiver ativo
                    animarCenario();
                    atualizarObstaculos();
                    gerarObstaculos(now);
                    verificarColisoes();
                }
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
        if (now - ultimoObstaculoTime > 1_000_000_000L) {
            if (random.nextDouble() < 0.6) {
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
    }

    private void atualizarObstaculos() {
        for (int i = obstaculos.size() - 1; i >= 0; i--) {
            Obstacle obstaculo = obstaculos.get(i);
            ObstacleView obstacleView = obstacleViews.get(i);
            
            obstaculo.update();
            obstacleView.updatePosition(obstaculo.getX(), obstaculo.getY());
            
            if (obstaculo.isForaDaTela()) {
                root.getChildren().remove(obstacleView.getView());
                obstaculos.remove(i);
                obstacleViews.remove(i);
            }
        }
    }

    private void verificarColisoes() {
        for (Obstacle obstaculo : obstaculos) {
            if (colidiuComPlayer(obstaculo)) {
                tratarColisao(obstaculo);
                break; // Só processa uma colisão por frame
            }
        }
    }

    private boolean colidiuComPlayer(Obstacle obstaculo) {
        double playerX = playerModel.getX();
        double playerY = playerModel.getY();
        double playerWidth = 40;
        double playerHeight = 70;
        
        // ✅ COLISÃO MAIS PRECISA
        boolean colidindo = playerX < obstaculo.getX() + obstaculo.getWidth() &&
                           playerX + playerWidth > obstaculo.getX() &&
                           playerY < obstaculo.getY() + obstaculo.getHeight() &&
                           playerY + playerHeight > obstaculo.getY();
        
        // ✅ DEBUG (descomente se quiser ver as colisões)
        if (colidindo) {
            System.out.println("💥 Colisão detectada com: " + obstaculo.getTipo());
        }
        
        return colidindo;
    }

    private void tratarColisao(Obstacle obstaculo) {
        // ✅ REMOVE o obstáculo com que colidiu
        int index = obstaculos.indexOf(obstaculo);
        if (index != -1) {
            root.getChildren().remove(obstacleViews.get(index).getView());
            obstaculos.remove(index);
            obstacleViews.remove(index);
        }
        
        // ✅ PERDE UMA VIDA
        vidas--;
        System.out.println("💔 Colisão! Vidas restantes: " + vidas);
        
        // ✅ VERIFICA FIM DE JOGO
        if (vidas <= 0) {
            gameOver();
        } else {
            System.out.println("🚗 Continue! Vidas: " + vidas);
        }
    }

    private void gameOver() {
        jogoAtivo = false; // ✅ PARA O JOGO sem parar o game loop
        System.out.println("🎮 GAME OVER! Sem vidas restantes.");
        
        // O cenário continua se movendo, mas o jogador não pode mais jogar
        // Você pode adicionar uma mensagem na tela depois
    }

    public void moverCarroEsquerda() {
        playerModel.moverEsquerda();
        playerView.setPosition(playerModel.getX(), playerModel.getY());
    }
    
    public void moverCarroDireita() {
        playerModel.moverDireita();
        playerView.setPosition(playerModel.getX(), playerModel.getY());
    }
    
    public Pane getRoot() { 
        root.requestFocus();
        return root; 
    }
}