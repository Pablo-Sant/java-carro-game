package com.example.model;

public class SemaforoFechadoFactory extends ObstacleFactory {
    @Override
    public Obstacle criarObstaculo(double x, double y, double velocidade) {
        return new Obstacle(x, y, 40, 100, velocidade, "SEMAFORO_FECHADO", "/images/semaforo_fechado.png");
    }
}