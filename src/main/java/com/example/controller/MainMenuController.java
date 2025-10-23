package com.example.controller;

import com.example.model.ScoreManager;
import com.example.model.Settings;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import java.io.IOException;

public class MainMenuController {

    @FXML private Button startButton;
    @FXML private Button scoresButton;
    @FXML private Button muteButton;
    @FXML private ListView<String> scoresList;

    @FXML
    private void onStart() throws IOException {
        // troca para o jogo
        // usamos App.setRoot para voltar para a cena do jogo
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
        muteButton.setText(Settings.isMuted() ? "Som: Off" : "Som: On");
    }
}
