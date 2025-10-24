package com.example.model;

public class GameModel implements Subject {
    private final GameEventPublisher publisher;
    private double velocidadeJogo;
    private int pontuacao;
    private int vidas;
    private long tempoJogo;
    private boolean jogoAtivo;
    
    public GameModel() {
        this.publisher = new GameEventPublisher();
        this.velocidadeJogo = 3;
        this.pontuacao = 0;
        this.vidas = 8;
        this.jogoAtivo = true;
        this.tempoJogo = 0;
    }
    
    @Override
    public void addObserver(GameObserver observer) {
        publisher.addObserver(observer);
    }
    
    @Override
    public void removeObserver(GameObserver observer) {
        publisher.removeObserver(observer);
    }
    
    @Override
    public void notifyObservers(GameEvent.Type type, Object data) {
        publisher.notifyObservers(type, data);
    }
    
    public void aumentarDificuldade() {
        if (pontuacao % 100 == 0 && pontuacao > 0) {
            velocidadeJogo += 0.5;
            System.out.println("Velocidade aumentada para: " + velocidadeJogo);
        }
    }
    
    public void incrementarPontuacao(int valor) {
        pontuacao += valor;
        notifyObservers(GameEvent.Type.SCORE_CHANGED, pontuacao);
    }
    
    public void diminuirVidas(int quantidade) {
        vidas -= quantidade;
        notifyObservers(GameEvent.Type.LIVES_CHANGED, vidas);
        if (vidas <= 0) {
            jogoAtivo = false;
            notifyObservers(GameEvent.Type.GAME_OVER, tempoJogo);
        }
    }
    
    public void atualizarTempo(long novoTempo) {
        this.tempoJogo = novoTempo;
        notifyObservers(GameEvent.Type.TIMER_UPDATED, formatarTempo(novoTempo));
    }
    
    private String formatarTempo(long segundos) {
        long minutos = segundos / 60;
        long segs = segundos % 60;
        return String.format("%d:%02d", minutos, segs);
    }
    
    public double getVelocidadeJogo() { return velocidadeJogo; }
    public int getPontuacao() { return pontuacao; }
    public int getVidas() { return vidas; }
    public long getTempoJogo() { return tempoJogo; }
    public boolean isJogoAtivo() { return jogoAtivo; }
}