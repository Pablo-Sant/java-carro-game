package com.example.model;

public class SemaforoAbertoFactory extends ObstacleFactory {
    @Override
    public Obstacle criarObstaculo(double x, double y, double velocidade) {
        return new Obstacle(x, y, 40, 100, velocidade, "SEMAFORO_ABERTO", "/images/semaforo_aberto.png");
    }
}