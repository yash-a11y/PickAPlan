package com.example.pickaplan.features;

import android.content.Context;
import android.util.Log;
import java.io.*;
import java.util.*;

public class SearchFrequencyTracker {

    private final String logFileName = "search.txt"; // Name of the log file
    private final HashMap<String, Integer> searchFrequencyMap;
    private final List<String> data;
    private final Context context;

    // Constructor to initialize the HashMap, data list, and context
    public SearchFrequencyTracker(Context context) {
        this.context = context;
        searchFrequencyMap = new HashMap<>();
        data = new ArrayList<>();
        loadSearchFrequenciesFromLog(); // Load initial frequencies from the log file
    }

    // Function to perform KMP search and update frequency
    public void search(String pattern) {
        // Update search frequency in the HashMap
        searchFrequencyMap.put(pattern, searchFrequencyMap.getOrDefault(pattern, 0) + 1);
        // Additional logic for KMP search can go here
    }

    // Load search frequencies from the log file on external storage
    public ArrayList<String> loadSearchFrequenciesFromLog() {
        ArrayList<String> searchFreq = new ArrayList<>();
        File file = new File(context.getExternalFilesDir(null), logFileName);
        if (!file.exists()) {
            Log.d("SearchFrequencyTracker", "Log file not found on external storage. Starting fresh.");
            return searchFreq;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                searchFreq.add(line);
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String word = parts[0];
                    int count = Integer.parseInt(parts[1]);
                    searchFrequencyMap.put(word, count);
                }
            }
        } catch (IOException e) {
            Log.e("SearchFrequencyTracker", "Error loading search frequencies: " + e.getMessage());
        }

        return searchFreq;
    }

    // Update the log file with the current search frequencies on external storage
    public void updateLogFile() {
        File file = new File(context.getExternalFilesDir(null), logFileName);
        Log.d("dir",context.getExternalFilesDir(null).toString());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Map.Entry<String, Integer> entry : searchFrequencyMap.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            Log.e("SearchFrequencyTracker", "Error updating the log file: " + e.getMessage());
        }
    }

    // Method to display the top 3 most searched terms
    public List<String> displayTopSearches() {
        List<String> topSearch = new ArrayList<>();
        System.out.println("Top 3 Searches:");
        searchFrequencyMap.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(3)
                .forEach(entry -> {
                    Log.d("searchf", "Word: " + entry.getKey() + ", Frequency: " + entry.getValue());
                    topSearch.add(entry.getKey()+"("+entry.getValue()+")");
                });

        return topSearch;
    }

    // Method to get the frequency of a searched word
    public int getSearchFrequency(String word) {
        return searchFrequencyMap.getOrDefault(word, 0);
    }
}
