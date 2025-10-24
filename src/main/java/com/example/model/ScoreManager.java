package com.example.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreManager {
    private static final String FILE = "top_times.txt";

    public static synchronized void addTime(long seconds) {
        List<Long> times = readTimes();
        times.add(seconds);
        Collections.sort(times, Collections.reverseOrder());
        
        if (times.size() > 10) times = new ArrayList<>(times.subList(0, 10));
        writeTimes(times);
    }

    public static synchronized List<Long> readTimes() {
        List<Long> times = new ArrayList<>();
        File f = new File(FILE);
        if (!f.exists()) return times;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                try {
                    times.add(Long.parseLong(line));
                } catch (NumberFormatException e) {
                    
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return times;
    }

    private static synchronized void writeTimes(List<Long> times) {
        File f = new File(FILE);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
            for (Long t : times) {
                bw.write(Long.toString(t));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized List<String> getTopTimesFormatted() {
        List<Long> times = readTimes();
        List<String> out = new ArrayList<>();
        for (Long s : times) {
            long minutes = s / 60;
            long seconds = s % 60;
            out.add(String.format("%d:%02d", minutes, seconds));
        }
        return out;
    }
}
