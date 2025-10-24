package com.example.model;

public class PlayerCar {
    private double x;
    private double y;
    private double velocidadeLateral = 30;
    
    public PlayerCar(double startX, double startY) {
        this.x = startX;
        this.y = startY;
    }
    
    public void moverEsquerda() {
        
        double novaX = x - velocidadeLateral;
        
        if (novaX >= 250) {
            x = novaX;
        }
    }
    
    public void moverDireita() {
        
        double novaX = x + velocidadeLateral;
        
        if (novaX <= 500) {
            x = novaX;
        }
    }
    
    
    public double getX() { return x; }
    public double getY() { return y; }
    public double getVelocidadeLateral() { return velocidadeLateral; }
    
    
    public void setVelocidadeLateral(double velocidade) {
        this.velocidadeLateral = velocidade;
    }
}