package com.example.model;

import java.util.Random;

public class ObstacleFactory {
    private static final Random random = new Random();
    
    public static Obstacle criarObstaculoAleatorio(double velocidadeJogo) {
        String[] tipos = {"BURACO", "PEDESTRE", "SEMAFORO", "CONE"};
        String tipo = tipos[random.nextInt(tipos.length)];
        
        // Posição aleatória na pista (entre 200 e 600)
        double posX = 200 + random.nextDouble() * 400;
        
        switch (tipo) {
            case "BURACO":
                return new Obstacle(posX, -100, 60, 60, velocidadeJogo, "BURACO", "/images/buraco.png");
            case "PEDESTRE":
                return new Obstacle(posX, -100, 40, 80, velocidadeJogo, "PEDESTRE", "/images/pedestre.png");
            case "SEMAFORO":
                return new Obstacle(posX, -100, 40, 100, velocidadeJogo, "SEMAFORO", "/images/semaforo.png");
            case "CONE":
                return new Obstacle(posX, -100, 50, 70, velocidadeJogo, "CONE", "/images/cone.png");
            default:
                return new Obstacle(posX, -100, 50, 50, velocidadeJogo, "DEFAULT", "/images/buraco.png");
        }
    }
}