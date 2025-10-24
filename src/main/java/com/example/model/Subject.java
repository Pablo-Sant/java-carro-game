package com.example.model;

public interface Subject {
    void addObserver(GameObserver observer);
    void removeObserver(GameObserver observer);
    void notifyObservers(GameEvent.Type type, Object data);
}