package com.example.controller;

import com.example.model.ScoreManager;
import com.example.model.Settings;
import com.example.model.AudioManager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;

public class MainMenuController {

    @FXML private Button startButton;
    @FXML private Button scoresButton;
    @FXML private Button muteButton;
    @FXML private ListView<String> scoresList;
    @FXML private ImageView logoImage;

    @FXML
    public void initialize() {
        AudioManager.initBackgroundMusic();
        try {
            var is = getClass().getResourceAsStream("/images/ui/carrologo.png");
            if (is != null && logoImage != null) {
                logoImage.setImage(new Image(is));
                logoImage.toFront();
            } else {
                System.err.println("Logo not found in classpath: /images/ui/carrologo.png");
            }
        } catch (Exception ex) {
            System.err.println("Erro ao carregar logo: " + ex.getMessage());
        }
    }

    @FXML
    private void onStart() throws IOException {
        
        com.example.App.setRoot("primary");
    }

    @FXML
    private void onShowScores() {
        scoresList.getItems().clear();
        scoresList.getItems().addAll(ScoreManager.getTopTimesFormatted());
    }

    @FXML
    private void onToggleMute() {
        Settings.toggleMute();
        AudioManager.toggleMute();
        muteButton.setText(AudioManager.isMuted() ? "Som: Off" : "Som: On");
    }

    @FXML
    private void onSair() {
        System.exit(0);
    }
}
