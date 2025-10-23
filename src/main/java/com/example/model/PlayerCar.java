package com.example.model;

public class PlayerCar {
    private double x;        // Posição horizontal
    private double y;        // Posição vertical (fixa)
    private double velocidadeLateral = 20;  // Velocidade de movimento lateral
    
    public PlayerCar(double startX, double startY) {
        this.x = startX;
        this.y = startY;
    }
    
    public void moverEsquerda() {
        // Move para esquerda DIMINUINDO a posição X
        double novaX = x - velocidadeLateral;
        // Limite esquerdo mais estreito (200 + margem para o carro não encostar)
        if (novaX >= 250) {
            x = novaX;
        }
    }
    
    public void moverDireita() {
        // Move para direita AUMENTANDO a posição X
        double novaX = x + velocidadeLateral;
        // Limite direito mais estreito (600 - largura do carro - margem)
        if (novaX <= 500) {
            x = novaX;
        }
    }
    
    //  Métodos de acesso
    public double getX() { return x; }
    public double getY() { return y; }
    public double getVelocidadeLateral() { return velocidadeLateral; }
    
    // Se quiser ajustar a velocidade lateral posteriormente
    public void setVelocidadeLateral(double velocidade) {
        this.velocidadeLateral = velocidade;
    }
}