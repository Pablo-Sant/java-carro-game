package com.example.model;

public class PlayerCar {
    private double x;
    private double y;
    private double velocidade = 5;
    
    public PlayerCar(double startX, double startY) {
        this.x = startX;
        this.y = startY;
    }
    
    public void moverEsquerda() {
        //  Limites ajustados para a pista de 500px de largura
        if (x > 170) { // Não sai da pista (150 + margem)
            x -= velocidade;
        }
    }
    
    public void moverDireita() {
        //  Limites ajustados para a pista de 500px de largura  
        if (x < 630) { // Não sai da pista (650 - largura carro - margem)
            x += velocidade;
        }
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
}