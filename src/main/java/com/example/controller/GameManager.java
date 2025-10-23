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
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import com.example.model.ScoreManager;
import java.io.IOException;

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
    private final int VidasIniciais = 5;
    private int vidas = VidasIniciais; // ✅ COMEÇA COM 5 VIDAS
    private boolean jogoAtivo = true; // ✅ CONTROLE DE ESTADO DO JOGO
    private Label vidasLabel; // Label para exibir vidas na UI
    private VBox gameOverBox; // container da tela de game over
    private Button reiniciarButton;
    private Label timerLabel; // Label para exibir tempo sobrevivido (mm:ss)
    private long startTimeNano = 0; // tempo de início do round em nanos
    
    public GameManager() {
        root = new Pane();
        initializeGame();
        setupGameLoop();
    }
    
    private void initializeGame() {
        roadView = new RoadView();
        playerModel = new PlayerCar(375, 500); // Posição inicial mais centralizada
        playerView = new PlayerView();
        playerView.setPosition(playerModel.getX(), playerModel.getY());

        obstaculos = new ArrayList<>();
        obstacleViews = new ArrayList<>();
        random = new Random();

        
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
    // posiciona no canto superior direito com binding para adaptar largura
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
                    atualizarTimer(now);
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
    // minutos sem zero à esquerda, segundos com dois dígitos
    timerLabel.setText(String.format("%d:%02d", minutes, seconds));
    }
    
    private void animarCenario() {
        roadView.setVelocidade(velocidadeCenario);
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
        // Ajustando a hitbox para um tamanho menor e mais preciso
        double playerWidth = 40 * 3; // largura base * escala reduzida
        double playerHeight = 70 * 3; // altura base * escala reduzida
        
        // Ajustando a área de colisão ainda mais assimétrica
        double hitboxWidth = playerWidth * 0.5; // reduzindo a largura base
        double hitboxHeight = playerHeight * 0.6;
        
        // Aumentando o deslocamento para a esquerda (50% da largura)
        double offsetX = hitboxWidth * 0.5;
        
        // Posicionando a hitbox com ainda mais área à esquerda e menos à direita
        double hitboxX = playerX + (playerWidth - hitboxWidth) / 2 - offsetX;
        double hitboxY = playerY + (playerHeight - hitboxHeight) / 2;
        
        // ✅ COLISÃO MAIS PRECISA COM HITBOX AJUSTADA
        boolean colidindo = hitboxX < obstaculo.getX() + obstaculo.getWidth() &&
                           hitboxX + hitboxWidth > obstaculo.getX() &&
                           hitboxY < obstaculo.getY() + obstaculo.getHeight() &&
                           hitboxY + hitboxHeight > obstaculo.getY();
        
        // ✅ DEBUG
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
    atualizarVidasUI();
        
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
        // Atualiza label para indicar fim de jogo
        if (vidasLabel != null) {
            vidasLabel.setText("GAME OVER");
        }

        // O cenário continua se movendo, mas o jogador não pode mais jogar
        // Você pode adicionar uma mensagem na tela depois
        // opcional: mostrar tempo final no timerLabel
        if (timerLabel != null && startTimeNano != 0) {
            long elapsedNanos = System.nanoTime() - startTimeNano;
            long totalSeconds = elapsedNanos / 1_000_000_000L;
            long minutes = totalSeconds / 60;
            long seconds = totalSeconds % 60;
            timerLabel.setText(String.format("%d:%02d", minutes, seconds));
            // salva tempo em segundos no ScoreManager
            ScoreManager.addTime(totalSeconds);
        }
        // mostra o overlay de game over com botão reiniciar
        showGameOverScreen();
    }

    private void atualizarVidasUI() {
        if (vidasLabel != null) {
            vidasLabel.setText("Vidas: " + vidas);
        }
    }

    private void showGameOverScreen() {
        if (gameOverBox != null) return; // já mostrado

        // Overlay semitransparente em toda a tela
        javafx.scene.shape.Rectangle overlay = new javafx.scene.shape.Rectangle();
        overlay.widthProperty().bind(root.widthProperty());
        overlay.heightProperty().bind(root.heightProperty());
        overlay.setFill(new Color(0, 0, 0, 0.8));

        // Título Game Over
        Label title = new Label("GAME OVER");
        title.setFont(new Font(48));
        title.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        // Tempo final
        Label finalTime = new Label("Tempo: " + timerLabel.getText());
        finalTime.setFont(new Font(24));
        finalTime.setStyle("-fx-text-fill: white;");

        // Botão de voltar ao menu
        Button menuButton = new Button("Voltar ao Menu");
        menuButton.setStyle("-fx-font-size: 18; -fx-padding: 10 20;");
        menuButton.setOnAction(e -> {
            try {
                // Para o game loop antes de voltar ao menu
                if (gameLoop != null) {
                    gameLoop.stop();
                }
                // Limpa todos os elementos do jogo
                limparJogo();
                // Volta para o menu principal
                com.example.App.setRoot("main_menu");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // VBox centralizada na tela
        gameOverBox = new VBox(20); // aumentei o espaçamento entre elementos
        gameOverBox.setAlignment(Pos.CENTER);
        gameOverBox.getChildren().addAll(title, finalTime, menuButton);
        gameOverBox.layoutXProperty().bind(root.widthProperty().subtract(gameOverBox.widthProperty()).divide(2));
        gameOverBox.layoutYProperty().bind(root.heightProperty().subtract(gameOverBox.heightProperty()).divide(2));

        // Adiciona na ordem: overlay atrás, box na frente
        root.getChildren().add(overlay);
        root.getChildren().add(gameOverBox);
    }

    private void limparJogo() {
        // Remove overlay/gameOverBox
        if (gameOverBox != null) {
            // remove o overlay e o box
            List<javafx.scene.Node> toRemove = new ArrayList<>();
            for (javafx.scene.Node node : root.getChildren()) {
                if (node == gameOverBox) {
                    toRemove.add(node);
                } else if (node instanceof javafx.scene.shape.Rectangle) {
                    javafx.scene.shape.Rectangle r = (javafx.scene.shape.Rectangle) node;
                    if (r.getFill() instanceof Color && ((Color) r.getFill()).getOpacity() > 0.5) {
                        toRemove.add(node);
                    }
                }
            }
            root.getChildren().removeAll(toRemove);
            gameOverBox = null;
        }

        // Limpa obstáculos
        for (ObstacleView ov : obstacleViews) {
            root.getChildren().remove(ov.getView());
        }
        obstacleViews.clear();
        obstaculos.clear();

        // Reseta todos os estados
        vidas = VidasIniciais;
        ultimoObstaculoTime = 0;
        jogoAtivo = false;
        startTimeNano = 0;
    }

    private void reiniciarJogo() {
        // Usa o método limparJogo para limpar tudo primeiro
        limparJogo();
        if (gameOverBox != null) {
            // remove o overlay (assumimos que está imediatamente antes do box ou presente)
            // procuramos e removemos nodes extras adicionados
            List<javafx.scene.Node> toRemove = new ArrayList<>();
            for (javafx.scene.Node node : root.getChildren()) {
                if (node == gameOverBox) {
                    toRemove.add(node);
                } else if (node instanceof javafx.scene.shape.Rectangle) {
                    // remover o overlay semitransparente (preto com opacidade)
                    javafx.scene.shape.Rectangle r = (javafx.scene.shape.Rectangle) node;
                    if (r.getFill() instanceof Color && ((Color) r.getFill()).getOpacity() > 0.5) {
                        toRemove.add(node);
                    }
                }
            }
            root.getChildren().removeAll(toRemove);
            gameOverBox = null;
        }

        // Limpa obstáculos da cena e das listas
        for (ObstacleView ov : obstacleViews) {
            root.getChildren().remove(ov.getView());
        }
        obstacleViews.clear();
        obstaculos.clear();

        // Reseta estado
        vidas = VidasIniciais;
        atualizarVidasUI();
        ultimoObstaculoTime = 0;
        jogoAtivo = true;

        // Reseta posição do jogador
        playerModel = new PlayerCar(400, 500);
        playerView.setPosition(playerModel.getX(), playerModel.getY());

        // garante foco para controles
        root.requestFocus();
        // reinicia o timer
        startTimeNano = System.nanoTime();
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