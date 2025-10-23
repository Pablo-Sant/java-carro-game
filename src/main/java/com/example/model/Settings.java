package com.example.model;

public class Settings {
    private static boolean muted = false;

    public static boolean isMuted() { return muted; }
    public static void setMuted(boolean m) { muted = m; }
    public static void toggleMute() { muted = !muted; }
}
