package com.example.model;

public class PedestreEsquerdaFactory extends ObstacleFactory {
    @Override
    public Obstacle criarObstaculo(double x, double y, double velocidade) {
        return new Obstacle(x, y, 40, 80, velocidade, "PEDESTRE_ESQUERDA", "/images/pedestre_esquerda.png");
    }
}