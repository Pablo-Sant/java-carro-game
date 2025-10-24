package com.example.model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioManager {
    private static MediaPlayer mediaPlayer;
    private static boolean isMuted = false;

    public static void initBackgroundMusic() {
        try {
            System.out.println("Iniciando carregamento da música...");
            if (mediaPlayer != null) {
                try {
                    mediaPlayer.stop();
                    mediaPlayer.dispose();
                } catch (Exception ignore) {
                    
                }
                mediaPlayer = null;
            }
            var resource = AudioManager.class.getResource("/music/Top Gear - Title (ost snes) _ [BGM] [SFC] - トップレーサー(MP3_160K).mp3");
            if (resource == null) {
                System.out.println("Recurso não encontrado. Tentando caminho alternativo...");
                resource = AudioManager.class.getClassLoader().getResource("music/Top Gear - Title (ost snes) _ [BGM] [SFC] - トップレーサー(MP3_160K).mp3");
            }
            if (resource == null) {
                System.out.println("Arquivo de música não encontrado!");
                return;
            }
            String path = resource.toExternalForm();
            System.out.println("Caminho da música: " + path);
            
            Media sound = new Media(path);
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(0.5);
            System.out.println("MediaPlayer criado, iniciando playback...");
            mediaPlayer.play();
            System.out.println("Música iniciada!");
        } catch (Exception e) {
            System.out.println("Erro ao carregar a música: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void toggleMute() {
        isMuted = !isMuted;
        if (mediaPlayer != null) {
            if (isMuted) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.play();
            }
        }
    }

    public static boolean isMuted() {
        return isMuted;
    }

    public static void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}