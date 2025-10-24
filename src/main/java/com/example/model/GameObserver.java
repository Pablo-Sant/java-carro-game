package com.example.model;

public interface GameObserver {
    void onGameEvent(GameEvent.Type type, Object data);
}
