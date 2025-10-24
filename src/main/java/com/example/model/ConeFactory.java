package com.example.model;

public class ConeFactory extends ObstacleFactory {
    @Override
    public Obstacle criarObstaculo(double x, double y, double velocidade) {
        return new Obstacle(x, y, 50, 70, velocidade, "CONE", "/images/cone.png");
    }
}