package com.example.view;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RoadView {
    private Pane root;
    private List<Rectangle> faixas;
    
    public RoadView() {
        root = new Pane();
        faixas = new ArrayList<>();
        criarCenarioCompleto();
    }
    
    private void criarCenarioCompleto() {
        // Grama nas laterais 
        Rectangle gramaEsquerda = new Rectangle(0, 0, 150, 600);
        gramaEsquerda.setFill(Color.GREEN);
        
        Rectangle gramaDireita = new Rectangle(650, 0, 150, 600);
        gramaDireita.setFill(Color.GREEN);
        
        // Pista principal
        Rectangle pista = new Rectangle(150, 0, 500, 600);
        pista.setFill(Color.BLACK);
        
        // Faixas na pista
        criarFaixasDaPista();
        
        // Acostamentos
        Rectangle acostamentoEsquerdo = new Rectangle(140, 0, 10, 600);
        acostamentoEsquerdo.setFill(Color.GRAY);
        
        Rectangle acostamentoDireito = new Rectangle(650, 0, 10, 600);
        acostamentoDireito.setFill(Color.GRAY);
        
        // Adiciona todos os elementos ao root
        root.getChildren().addAll(
            gramaEsquerda, gramaDireita,
            pista,
            acostamentoEsquerdo, acostamentoDireito
        );
        root.getChildren().addAll(faixas);
    }
    
    private void criarFaixasDaPista() {
        // Cria várias faixas brancas ao longo da pista
        for (int i = 0; i < 20; i++) {
            Rectangle faixa = new Rectangle(395, i * 60, 10, 30);
            faixa.setFill(Color.WHITE);
            faixas.add(faixa);
        }
    }
    
    public Pane getRoot() {
        return root;
    }
    
    public List<Rectangle> getFaixas() {
        return faixas;
    }
}