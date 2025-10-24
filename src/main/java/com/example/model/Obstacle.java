package com.example.model;

public class Obstacle {
    private double x, y;
    private double width, height;
    private double velocidade;
    private String tipo;
    private String imagePath;
    
    public Obstacle(double x, double y, double width, double height, 
                   double velocidade, String tipo, String imagePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velocidade = velocidade;
        this.tipo = tipo;
        this.imagePath = imagePath;
    }
    
    public String getImagePath() { return imagePath; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public String getTipo() { return tipo; }
    
    public void update() {
        y += velocidade;
    }
    
    public boolean isForaDaTela() {
        return y > 600;
    }
}
