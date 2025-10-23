package com.example.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.InputStream;

public class PlayerView {
    private ImageView carroView;

    public PlayerView() {
        carroView = new ImageView();
    carroView.setFitWidth(40);
    carroView.setFitHeight(70);
    carroView.setPreserveRatio(true);
    // gira o carro 90 graus e aumenta o tamanho em 5x
    carroView.setRotate(90);
    carroView.setScaleX(5);
    carroView.setScaleY(5);
    carroView.setSmooth(true);

        // tenta carregar a imagem do recurso; se falhar, deixa o ImageView vazio
        try (InputStream is = getClass().getResourceAsStream("/images/carro.png")) {
            if (is != null) {
                Image img = new Image(is);
                carroView.setImage(img);
            } else {
                System.err.println("Recurso de imagem /images/carro.png não encontrado");
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar imagem do carro: " + e.getMessage());
        }
    }

    public void setPosition(double x, double y) {
        carroView.setLayoutX(x);
        carroView.setLayoutY(y);
    }

    public ImageView getView() {
        return carroView;
    }
}