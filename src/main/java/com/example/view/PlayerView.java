package com.example.view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PlayerView {
    private Rectangle carroView;
    
    public PlayerView() {
        //  Toda a configuração VISUAL fica aqui na View
        carroView = new Rectangle(40, 70, Color.RED);
        carroView.setArcWidth(10);  // Cantos arredondados
        carroView.setArcHeight(10); // Cantos arredondados
    }
    
    public void setPosition(double x, double y) {
        carroView.setLayoutX(x);
        carroView.setLayoutY(y);
    }
    
    public Rectangle getView() {
        return carroView;
    }
}