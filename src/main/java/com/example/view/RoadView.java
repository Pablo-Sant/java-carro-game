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
    private double velocidade = 2.0;
    
    public RoadView() {
        root = new Pane();
        criarCenarioCompleto();
    }
    
    private void criarCenarioCompleto() {
        try {
            Image imagemCenario1 = new Image(getClass().getResourceAsStream("/images/cenario1.png"));
            Image imagemCenario2 = new Image(getClass().getResourceAsStream("/images/cenario2.png"));

            
            
            cenario1 = new ImageView(imagemCenario1);
            cenario2 = new ImageView(imagemCenario2);
            
            cenario1.setFitWidth(800);
            cenario1.setFitHeight(600);
            cenario2.setFitWidth(800);
            cenario2.setFitHeight(600);
            
            cenario1.setY(0);
            cenario2.setY(-600);
            
            root.getChildren().addAll(cenario1, cenario2);
            
        } catch (Exception e) {
            System.err.println("Erro ao carregar imagens dos cenários: " + e.getMessage());
        }
    }
    
    public void atualizarCenario() {
        cenario1.setY(cenario1.getY() + velocidade);
        cenario2.setY(cenario2.getY() + velocidade);
        
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