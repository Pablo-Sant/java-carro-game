package com.example.model;

import java.util.Random;

public class ObstacleFactory {
    private static final Random random = new Random();
    
    public static Obstacle criarObstaculoAleatorio(double velocidadeJogo) {
        String[] tipos = {"ARBUSTO", "PEDESTRE_DIREITA", "SEMAFORO_FECHADO", "CONE", "SEMAFORO_ABERTO", "PEDESTRE_ESQUERDA"};
        String tipo = tipos[random.nextInt(tipos.length)];
        
        double minX = 280;
        double maxX = 500;
        double posX = minX + random.nextDouble() * (maxX - minX);
    
   
        
        // Pequena variação aleatória reduzida para manter mais alinhado
        posX += (random.nextDouble() - 0.5) * 10; // Variação de ±5 pixels
        
        switch (tipo) {
            case "ARBUSTO":
                return new Obstacle(posX, -100, 60, 60, velocidadeJogo, "ARBUSTO", "/images/arbusto.png");
            case "PEDESTRE_DIREITA":
                return new Obstacle(posX, -100, 40, 80, velocidadeJogo, "PEDESTRE_DIREITA", "/images/pedestre_direita.png");
            case "PEDESTRE_ESQUERDA":
                return new Obstacle(posX, -100, 40, 80, velocidadeJogo, "PEDESTRE_ESQUERDA", "/images/pedestre_esquerda.png");
            case "SEMAFORO_FECHADO":
                return new Obstacle(posX, -100, 40, 100, velocidadeJogo, "SEMAFORO_FECHADO", "/images/semaforo_fechado.png");
            case "SEMAFORO_ABERTO":
                return new Obstacle(posX, -100, 40, 100, velocidadeJogo, "SEMAFORO_ABERTO", "/images/semaforo_aberto.png");
            case "CONE":
                return new Obstacle(posX, -100, 50, 70, velocidadeJogo, "CONE", "/images/cone.png");
            default:
                return new Obstacle(posX, -100, 50, 50, velocidadeJogo, "DEFAULT", "/images/Pedra1.png");
        }
    }
}