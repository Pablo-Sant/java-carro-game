package com.example.model;

public class ArbustoFactory extends ObstacleFactory {
    @Override
    public Obstacle criarObstaculo(double x, double y, double velocidade) {
        return new Obstacle(x, y, 60, 60, velocidade, "ARBUSTO", "/images/arbusto.png");
    }
}