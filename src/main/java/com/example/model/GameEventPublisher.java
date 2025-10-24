package com.example.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameEventPublisher implements Subject {
    private final List<GameObserver> observers = new ArrayList<>();

    public synchronized void addObserver(GameObserver o) {
        if (o == null) return;
        observers.add(o);
    }

    public synchronized void removeObserver(GameObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(GameEvent.Type type, Object data) {
        GameEvent event = new GameEvent(type, data);
        List<GameObserver> snapshot;
        synchronized (this) {
            snapshot = new ArrayList<>(observers);
        }
        for (GameObserver o : snapshot) {
            try {
                o.onGameEvent(event.getType(), event.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<GameObserver> getObservers() {
        return Collections.unmodifiableList(observers);
    }
}
