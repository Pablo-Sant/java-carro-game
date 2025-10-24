package com.example.model;

public abstract class ObstacleFactory {
    public abstract Obstacle criarObstaculo(double x, double y, double velocidade);
    
    public Obstacle criarObstaculoAleatorio(double velocidade) {
        double minX = 280;
        double maxX = 500;
        double posX = minX + Math.random() * (maxX - minX);
    posX += (Math.random() - 0.5) * 10;
        
        return criarObstaculo(posX, -100, velocidade);
    }
}