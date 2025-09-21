package com.example.model;

public class GameModel {
    private double velocidadeJogo = 3;  //  Velocidade de rolagem do cenário
    private int pontuacao = 0;
    
    public void aumentarDificuldade() {
        // Aumenta velocidade progressivamente
        if (pontuacao % 100 == 0 && pontuacao > 0) {
            velocidadeJogo += 0.5;
            System.out.println("Velocidade aumentada para: " + velocidadeJogo);
        }
    }
    
    public double getVelocidadeJogo() { return velocidadeJogo; }
    public int getPontuacao() { return pontuacao; }
    public void incrementarPontuacao() { pontuacao += 10; }
}