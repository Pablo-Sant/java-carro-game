package com.example.model;

public class PedestreDireitaFactory extends ObstacleFactory {
    @Override
    public Obstacle criarObstaculo(double x, double y, double velocidade) {
        return new Obstacle(x, y, 40, 80, velocidade, "PEDESTRE_DIREITA", "/images/pedestre_direita.png");
    }
}