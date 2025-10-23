package com.example.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.model.Obstacle;
import com.example.model.ObstacleFactory;
import com.example.model.PlayerCar;
import com.example.model.ScoreManager;
import com.example.view.ObstacleView;
import com.example.view.PlayerView;
import com.example.view.RoadView;

import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

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
    
    private final long INTERVALO_AUMENTO = 3_000_000_000L;
    
    private Label vidasLabel;
    private VBox gameOverBox;
    private Button reiniciarButton;
    private Label timerLabel;
    private long startTimeNano = 0;
    
    public GameManager() {
        root = new Pane();
        initializeGame();
        setupGameLoop();
    }
    
    private void initializeGame() {
        roadView = new RoadView();
        playerModel = new PlayerCar(375, 500);
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
        
        // Label de vidas no canto superior esquerdo
        vidasLabel = new Label("Vidas: " + vidas);
        vidasLabel.setFont(new Font(18));
        vidasLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        vidasLabel.setLayoutX(10);
        vidasLabel.setLayoutY(10);
        root.getChildren().add(vidasLabel);
        
        // Label do timer no canto superior direito
        timerLabel = new Label("00:00");
        timerLabel.setFont(new Font(18));
        timerLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        timerLabel.setLayoutY(10);
        timerLabel.layoutXProperty().bind(root.widthProperty().subtract(70));
        root.getChildren().add(timerLabel);
        
        // iniciar contador
        startTimeNano = System.nanoTime();
        
        configurarControles();
        System.out.println("🎮 Jogo iniciado! Vidas: " + vidas);
        atualizarVidasUI();
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
    
    private void setupGameLoop() {
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
                    atualizarTimer(now);
                    verificarAumentoVelocidadePorTempo(now); // ✅ ADICIONADO
                }
            }
        };
        gameLoop.start();
    }

    private void atualizarTimer(long now) {
        if (timerLabel == null || startTimeNano == 0) return;
        long elapsedNanos = now - startTimeNano;
        long totalSeconds = elapsedNanos / 1_000_000_000L;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        timerLabel.setText(String.format("%d:%02d", minutes, seconds));
    }
    
    private void animarCenario() {
        // ✅ MODIFICADO: Usa velocidadeAtual em vez de valor fixo
        roadView.setVelocidade(velocidadeAtual);
        roadView.atualizarCenario();
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
        Obstacle obstaculo = ObstacleFactory.criarObstaculoAleatorio(velocidadeAtual);

        double distanciaVertical = Math.abs(obstaculo.getY() - playerModel.getY());
            if (distanciaVertical < 100) { // Se nascer muito perto, reposiciona
            obstaculo = ObstacleFactory.criarObstaculoAleatorio(velocidadeAtual);
        }

   
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
                
                pontuacao += 10;
                System.out.println("⭐ Pontuação: " + pontuacao);
            }
        }
    }

    private void verificarAumentoVelocidadePorTempo(long now) {
        if (now - ultimoAumentoTime >= INTERVALO_AUMENTO) {
            aumentarVelocidade();
            ultimoAumentoTime = now;

            long tempoDecorrido = (now - inicioJogoTime) / 1_000_000_000;
            System.out.println("⏰ Tempo decorrido: " + tempoDecorrido + "s");
            System.out.println("🚀 VELOCIDADE AUMENTADA: " + String.format("%.1f", velocidadeAtual));
        }
    }

    private void aumentarVelocidade() {
        double aumento = 0.8;
        velocidadeAtual += aumento;
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
    double playerX = playerModel.getX();
    double playerY = playerModel.getY();
    double playerWidth = 40;
    double playerHeight = 70;
    
    double obstacleX = obstaculo.getX();
    double obstacleY = obstaculo.getY();
    double obstacleWidth = obstaculo.getWidth();
    double obstacleHeight = obstaculo.getHeight();
    
    // ✅ COLISÃO SIMPLES E PRECISA
    boolean colisaoHorizontal = playerX < obstacleX + obstacleWidth && 
                               playerX + playerWidth > obstacleX;
    
    boolean colisaoVertical = playerY < obstacleY + obstacleHeight && 
                             playerY + playerHeight > obstacleY;
    
    boolean colidindo = colisaoHorizontal && colisaoVertical;
    
    // ✅ DEBUG MELHORADO - COM NOME DO OBSTÁCULO
    if (colidindo) {
        System.out.println("💥 COLISÃO REAL DETECTADA:");
        System.out.println("   Carro:     X=" + playerX + " Y=" + playerY + " W=" + playerWidth + " H=" + playerHeight);
        System.out.println("   Obstáculo: " + obstaculo.getTipo() + " | X=" + obstacleX + " Y=" + obstacleY + " W=" + obstacleWidth + " H=" + obstacleHeight);
        System.out.println("   Sobreposição X: " + colisaoHorizontal);
        System.out.println("   Sobreposição Y: " + colisaoVertical);
        
        // ✅ CALCULA A SOBREPOSIÇÃO REAL
        double overlapX = Math.min(playerX + playerWidth, obstacleX + obstacleWidth) - Math.max(playerX, obstacleX);
        double overlapY = Math.min(playerY + playerHeight, obstacleY + obstacleHeight) - Math.max(playerY, obstacleY);
        System.out.println("   Sobreposição real: X=" + String.format("%.1f", overlapX) + "px, Y=" + String.format("%.1f", overlapY) + "px");
        System.out.println("   📍 Tipo do obstáculo: " + obstaculo.getTipo());
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
        
        
        int dano = calcularDano(obstaculo.getTipo());
        vidas -= dano;
        
        System.out.println("Colisão com " + obstaculo.getTipo() + "! Perdeu " + dano + " vida(s)");
        System.out.println("Vidas restantes: " + vidas);
        atualizarVidasUI();
        
        if (vidas <= 0) {
            gameOver();
        } else {
            System.out.println("Continue! Vidas: " + vidas);
        }
    }

    private int calcularDano(String tipoObstaculo) {
        switch (tipoObstaculo) {
            case "PEDESTRE_DIREITA":
                return 3;
            case "PEDESTRE_ESQUERDA":
                return 3;
            case "SEMAFORO_FECHADO":
                return 2;
            case "SEMAFORO_ABERTO":
                return 1;  
            case "BURACO":
                return 2;  
            case "CONE":
                return 1;  
            case "ARBUSTO":
                return 1;  
            default:
                return 1;  
        }
    }

    private void gameOver() {
        jogoAtivo = false;
        System.out.println("GAME OVER! Sem vidas restantes.");
        
        if (vidasLabel != null) {
            vidasLabel.setText("GAME OVER");
        }

        if (timerLabel != null && startTimeNano != 0) {
            long elapsedNanos = System.nanoTime() - startTimeNano;
            long totalSeconds = elapsedNanos / 1_000_000_000L;
            long minutes = totalSeconds / 60;
            long seconds = totalSeconds % 60;
            timerLabel.setText(String.format("%d:%02d", minutes, seconds));
            ScoreManager.addTime(totalSeconds);
        }
        
        showGameOverScreen();
    }

    private void atualizarVidasUI() {
        if (vidasLabel != null) {
            vidasLabel.setText("Vidas: " + vidas);
        }
    }

    private void showGameOverScreen() {
        if (gameOverBox != null) return;

        Rectangle overlay = new Rectangle();
        overlay.widthProperty().bind(root.widthProperty());
        overlay.heightProperty().bind(root.heightProperty());
        overlay.setFill(new Color(0, 0, 0, 0.8));

        Label title = new Label("GAME OVER");
        title.setFont(new Font(48));
        title.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Label finalTime = new Label("Tempo: " + timerLabel.getText());
        finalTime.setFont(new Font(24));
        finalTime.setStyle("-fx-text-fill: white;");

        Button menuButton = new Button("Voltar ao Menu");
        menuButton.setStyle("-fx-font-size: 18; -fx-padding: 10 20;");
        menuButton.setOnAction(e -> {
            try {
                if (gameLoop != null) {
                    gameLoop.stop();
                }
                limparJogo();
                com.example.App.setRoot("main_menu");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        gameOverBox = new VBox(20);
        gameOverBox.setAlignment(Pos.CENTER);
        gameOverBox.getChildren().addAll(title, finalTime, menuButton);
        gameOverBox.layoutXProperty().bind(root.widthProperty().subtract(gameOverBox.widthProperty()).divide(2));
        gameOverBox.layoutYProperty().bind(root.heightProperty().subtract(gameOverBox.heightProperty()).divide(2));

        root.getChildren().add(overlay);
        root.getChildren().add(gameOverBox);
    }

    private void limparJogo() {
        if (gameOverBox != null) {
            List<javafx.scene.Node> toRemove = new ArrayList<>();
            for (javafx.scene.Node node : root.getChildren()) {
                if (node == gameOverBox) {
                    toRemove.add(node);
                } else if (node instanceof Rectangle) {
                    Rectangle r = (Rectangle) node;
                    if (r.getFill() instanceof Color && ((Color) r.getFill()).getOpacity() > 0.5) {
                        toRemove.add(node);
                    }
                }
            }
            root.getChildren().removeAll(toRemove);
            gameOverBox = null;
        }

        for (ObstacleView ov : obstacleViews) {
            root.getChildren().remove(ov.getView());
        }
        obstacleViews.clear();
        obstaculos.clear();

        vidas = 5;
        ultimoObstaculoTime = 0;
        jogoAtivo = false;
        startTimeNano = 0;
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