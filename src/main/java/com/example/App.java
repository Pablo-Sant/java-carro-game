package com.example;

import com.example.controller.GameManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {
    private static Scene scene;
    private static Stage mainStage;

    @Override
    public void start(Stage stage) {
        try {
            mainStage = stage;
            scene = new Scene(loadFXML("main_menu"), 800, 600);
            stage.setTitle("Jogo de Carro - Menu");
            stage.setScene(scene);
            stage.show();

            System.out.println("Menu principal carregado com sucesso!");

        } catch (Exception e) {
            System.err.println("Erro ao iniciar a aplicação:");
            e.printStackTrace();
        }
    }

    public static void setRoot(String fxml) throws IOException {
        if ("primary".equals(fxml)) {
            GameManager gameManager = new GameManager();
            scene.setRoot(gameManager.getRoot());
            gameManager.getRoot().requestFocus();
            mainStage.setTitle("Jogo de Carro - Em Jogo");
        } else if ("main_menu".equals(fxml)) {
            scene.setRoot(loadFXML("main_menu"));
            mainStage.setTitle("Jogo de Carro - Menu Principal");
        } else {
            scene.setRoot(loadFXML(fxml));
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }
}