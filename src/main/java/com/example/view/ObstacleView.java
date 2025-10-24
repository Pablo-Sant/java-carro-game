package com.example.view;

import com.example.model.Obstacle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ObstacleView {
    private ImageView imageView;
    
    public ObstacleView(Obstacle obstacle) {
        try {
            Image image = new Image(getClass().getResourceAsStream(obstacle.getImagePath()));
            imageView = new ImageView(image);
            imageView.setFitWidth(obstacle.getWidth());
            imageView.setFitHeight(obstacle.getHeight());
            imageView.setLayoutX(obstacle.getX());
            imageView.setLayoutY(obstacle.getY());
            
        } catch (Exception e) {
            System.err.println("Erro ao carregar imagem: " + obstacle.getImagePath());
            e.printStackTrace();
            
            imageView = new ImageView();
            imageView.setFitWidth(obstacle.getWidth());
            imageView.setFitHeight(obstacle.getHeight());
            imageView.setLayoutX(obstacle.getX());
            imageView.setLayoutY(obstacle.getY());
        }
    }
    
    public ImageView getView() {
        return imageView;
    }
    
    public void updatePosition(double x, double y) {
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
    }
}