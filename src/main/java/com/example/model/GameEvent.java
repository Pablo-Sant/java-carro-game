package com.example.model;

public class GameEvent {
    public enum Type {
        LIVES_CHANGED,
        SCORE_CHANGED,
        TIMER_UPDATED,
        GAME_OVER
    }

    private final Type type;
    private final Object data;

    public GameEvent(Type type, Object data) {
        this.type = type;
        this.data = data;
    }

    public Type getType() { return type; }
    public Object getData() { return data; }
}
