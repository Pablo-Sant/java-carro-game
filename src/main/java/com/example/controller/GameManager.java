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
    private List<Obstacle> obstaculos;
    private List<ObstacleView> obstacleViews;
    private Random random;
    private long ultimoObstaculoTime = 0;
    private int vidas = 5;
    private boolean jogoAtivo = true;
    private double velocidadeBase = 4;
    private long inicioJogoTime = 0;
    private long ultimoAumentoTime = 0;
    private double velocidadeAtual;
    private int pontuacao = 0;
    
    // Aumenta a velocidade a cada 4 segundos
    private final long INTERVALO_AUMENTO = 3_000_000_000L;
    
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

        velocidadeAtual = velocidadeBase;
        inicioJogoTime = 0;
        ultimoAumentoTime = 0;

        root.getChildren().add(roadView.getRoot());
        root.getChildren().add(playerView.getView());
        
        configurarControles();
        System.out.println("Jogo iniciado! Vidas: " + vidas + " Velocidade: " + velocidadeAtual);
    }
    
    private void configurarControles() {
        root.setOnKeyPressed(event -> {
            if (!jogoAtivo) return;
            
            if (event.getCode() == KeyCode.LEFT) {
                moverCarroEsquerda();
            } else if (event.getCode() == KeyCode.RIGHT) {
                moverCarroDireita();
            }
        });
        
        root.setFocusTraversable(true);
        root.requestFocus();
    }
    
    private void setupGameLoop() { // Métodos que se repetem quando o jogo está ativo
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(inicioJogoTime == 0){
                    inicioJogoTime = now;
                    ultimoAumentoTime = now;
                }

                if (jogoAtivo) {
                    animarCenario();
                    atualizarObstaculos();
                    gerarObstaculos(now);
                    verificarColisoes();
                    verificarAumentoVelocidadePorTempo(now); 
                }
            }
        };
        gameLoop.start();
    }
    
    private void animarCenario() {
        List<Rectangle> faixas = roadView.getFaixas();
        for (Rectangle faixa : faixas) {
            faixa.setLayoutY(faixa.getLayoutY() + velocidadeAtual);
            if (faixa.getLayoutY() > 600) { 
                faixa.setLayoutY(-30); 
            }
        }
    }

    private void gerarObstaculos(long now) {
        if (now - ultimoObstaculoTime > 1_000_000_000L) { // A cada 1 segundo
            if (random.nextDouble() < 0.6) { // 60% de chance de gerar a cada 1 segundo para não ficar previsível
                criarNovoObstaculo();
            }
            ultimoObstaculoTime = now;
        }
    }

    private void criarNovoObstaculo() {
        Obstacle obstaculo = ObstacleFactory.criarObstaculoAleatorio(velocidadeAtual); // Cria o modelo do obstáculo
        ObstacleView obstacleView = new ObstacleView(obstaculo); // Cria o visual do obstáculo
    
        obstaculos.add(obstaculo); // Adiciona na lista para verificar colisões e remover
        obstacleViews.add(obstacleView);
        root.getChildren().add(obstacleView.getView()); // Adiciona na tela
    }

    private void atualizarObstaculos() { // Faz os obstáculos se moverem e remove os que já sairam da tela
        for (int i = obstaculos.size() - 1; i >= 0; i--) {
            Obstacle obstaculo = obstaculos.get(i);
            ObstacleView obstacleView = obstacleViews.get(i);
            
            obstaculo.update(); // Atualiza posição lógica
            obstacleView.updatePosition(obstaculo.getX(), obstaculo.getY()); // Atualiza posição na tela
            
            if (obstaculo.isForaDaTela()) {
                root.getChildren().remove(obstacleView.getView());
                obstaculos.remove(i); // Remove obstáculos que estão foras do frame
                obstacleViews.remove(i);
                
                pontuacao += 10; // Dar 10 pontos por ter passado pelo obstáculo
                System.out.println("Pontuação: " + pontuacao);
                
            }
        }
    }

    
    private void verificarAumentoVelocidadePorTempo(long now) {
        if (now - ultimoAumentoTime >= INTERVALO_AUMENTO) { // Verifica se passarm 4 segundos desde o último aumento
            aumentarVelocidade();
            ultimoAumentoTime = now; // Guarda o momento do último aumento

            long tempoDecorrido = (now - inicioJogoTime) / 1_000_000_000;
            System.out.println("Tempo decorrido: " + tempoDecorrido + "s"); // Mostra tempo total de jogo
        }
    }

    
    private void aumentarVelocidade() {
        double aumento = 0.8;
        velocidadeAtual += aumento;
        System.out.println("VELOCIDADE AUMENTADA: " + String.format("%.1f", velocidadeAtual));
    }

    private void verificarColisoes() {
        for (Obstacle obstaculo : obstaculos) {
            if (colidiuComPlayer(obstaculo)) {
                tratarColisao(obstaculo);
                break;
            }
        }
    }

    private boolean colidiuComPlayer(Obstacle obstaculo) {
        double playerX = playerModel.getX(); // Pega posição horizontal do carro
        double playerY = playerModel.getY(); // Pega posição vertical do carro
        double playerWidth = 40; // Largura do carro
        double playerHeight = 70; // Altura do carro
        
        boolean colidindo = playerX < obstaculo.getX() + obstaculo.getWidth() &&
                           playerX + playerWidth > obstaculo.getX() &&
                           playerY < obstaculo.getY() + obstaculo.getHeight() &&
                           playerY + playerHeight > obstaculo.getY();
        
        if (colidindo) {
            System.out.println("Colisão detectada com: " + obstaculo.getTipo());
        }
        
        return colidindo;
    }

    private void tratarColisao(Obstacle obstaculo) {
        int index = obstaculos.indexOf(obstaculo);
        if (index != -1) {
            root.getChildren().remove(obstacleViews.get(index).getView());
            obstaculos.remove(index);
            obstacleViews.remove(index);
        }
        
        vidas--;
        System.out.println("Colisão! Vidas restantes: " + vidas);
        
        if (vidas <= 0) {
            gameOver();
        } else {
            System.out.println("Continue! Vidas: " + vidas);
        }
    }

    private void gameOver() {
        jogoAtivo = false;
        System.out.println("GAME OVER! Pontuação final: " + pontuacao);
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