package com.example.model;


    
import java.util.Random;

public class ObstacleCreator {
    private  final Random random = new Random();
    
    public Obstacle criarObstaculoAleatorio(double velocidadeJogo) {
        ObstacleFactory factory = escolherFactoryAleatoria();
        return factory.criarObstaculoAleatorio(velocidadeJogo);
    }
    
    private ObstacleFactory escolherFactoryAleatoria() {
        int escolha = random.nextInt(6);
        
        switch (escolha) {
            case 0:
                return new ArbustoFactory();
            case 1:
                return new PedestreDireitaFactory();
            case 2:
                return new PedestreEsquerdaFactory();
            case 3:
                return new SemaforoFechadoFactory();
            case 4:
                return new SemaforoAbertoFactory();
            case 5:
                return new ConeFactory();
            default:
                return new ConeFactory();
        }
    }
    public Obstacle criarObstaculoEspecifico(String tipo, double velocidadeJogo) {
        ObstacleFactory factory = criarFactoryPorTipo(tipo);
        return factory.criarObstaculoAleatorio(velocidadeJogo);
    }
    
    private ObstacleFactory criarFactoryPorTipo(String tipo) {
        switch (tipo) {
            case "ARBUSTO":
                return new ArbustoFactory();
            case "PEDESTRE_DIREITA":
                return new PedestreDireitaFactory();
            case "PEDESTRE_ESQUERDA":
                return new PedestreEsquerdaFactory();
            case "SEMAFORO_FECHADO":
                return new SemaforoFechadoFactory();
            case "SEMAFORO_ABERTO":
                return new SemaforoAbertoFactory();
            case "CONE":
                return new ConeFactory();
            default:
                throw new IllegalArgumentException("Tipo de obstáculo desconhecido: " + tipo);
        }
    }
}

