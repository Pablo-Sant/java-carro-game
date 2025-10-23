package com.example.view;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RoadView {
    private Pane root;
    private ImageView cenario1;
    private ImageView cenario2;
    private double velocidade = 2.0; // Velocidade de rolagem do cenário
    
    public RoadView() {
        root = new Pane();
        criarCenarioCompleto();
    }
    
    private void criarCenarioCompleto() {
        try {
            // Carrega as imagens dos cenários
            Image imagemCenario1 = new Image(getClass().getResourceAsStream("/images/cenario1.png"));
            Image imagemCenario2 = new Image(getClass().getResourceAsStream("/images/cenario2.png"));
            
            // Cria os ImageViews para os cenários
            cenario1 = new ImageView(imagemCenario1);
            cenario2 = new ImageView(imagemCenario2);
            
            // Configura o tamanho dos cenários para cobrir toda a tela
            cenario1.setFitWidth(800);
            cenario1.setFitHeight(600);
            cenario2.setFitWidth(800);
            cenario2.setFitHeight(600);
            
            // Posiciona o segundo cenário acima do primeiro
            cenario1.setY(0);
            cenario2.setY(-600);
            
            // Adiciona os cenários ao root (ordem importante: primeiro os cenários)
            root.getChildren().addAll(cenario1, cenario2);
            
        } catch (Exception e) {
            System.err.println("Erro ao carregar imagens dos cenários: " + e.getMessage());
        }
    }
    
    public void atualizarCenario() {
        // Move os cenários para baixo
        cenario1.setY(cenario1.getY() + velocidade);
        cenario2.setY(cenario2.getY() + velocidade);
        
        // Quando um cenário sai completamente da tela, move-o para cima do outro
        if (cenario1.getY() >= 600) {
            cenario1.setY(cenario2.getY() - 600);
        }
        if (cenario2.getY() >= 600) {
            cenario2.setY(cenario1.getY() - 600);
        }
    }
    
    public void setVelocidade(double velocidade) {
        this.velocidade = velocidade;
    }
    
    public Pane getRoot() {
        return root;
    }
}