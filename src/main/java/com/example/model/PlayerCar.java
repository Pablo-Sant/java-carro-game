package com.example.model;

public class PlayerCar {
    private double x;        // Posição horizontal
    private double y;        // Posição vertical 
    private double velocidadeLateral = 20;  // Velocidade de movimento lateral
    
    public PlayerCar(double startX, double startY) {
        this.x = startX;
        this.y = startY;
    }
    
    public void moverEsquerda() {
        // Move para esquerda diminuindo a posição X
        if (x > 200) { // Limite esquerdo da pista
            x -= velocidadeLateral;
        }
    }
    
    public void moverDireita() {
        // Move para direita aumentando a posição X  
        if (x < 600) { // Limite direito da pista
            x += velocidadeLateral;
        }
    }
    
    //  Métodos de acesso
    public double getX() { return x; }
    public double getY() { return y; }
    public double getVelocidadeLateral() { return velocidadeLateral; }
    
    
    public void setVelocidadeLateral(double velocidade) {
        this.velocidadeLateral = velocidade;
    }
}