package com.example.pickaplan.features;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class SearchFrequencyTracker {

    private final HashMap<String, Integer> searchFrequencyMap;
    private final List<String> data;
    private final String logFilePath;

    // Constructor to initialize the HashMap, data list, and log file path
    public SearchFrequencyTracker(String logFilePath) {
        searchFrequencyMap = new HashMap<>();
        data = new ArrayList<>();
        this.logFilePath = logFilePath;
        loadSearchFrequenciesFromLog(); // Load initial frequencies from the log file
    }

    // Function to perform KMP search and update frequency
    public void search(String pattern) {
        // Update search frequency in the HashMap
        searchFrequencyMap.put(pattern, searchFrequencyMap.getOrDefault(pattern, 0) + 1);

        // Search through the loaded data
        for (String line : data) {
            int[] lps = computeLPSArray(pattern);
            int i = 0, j = 0;
            while (i < line.length()) {
                if (pattern.charAt(j) == line.charAt(i)) {
                    i++;
                    j++;
                }
                if (j == pattern.length()) {
                    System.out.println("Pattern '" + pattern + "' found in line: " + line);
                    j = lps[j - 1];
                } else if (i < line.length() && pattern.charAt(j) != line.charAt(i)) {
                    if (j != 0) {
                        j = lps[j - 1];
                    } else {
                        i++;
                    }
                }
            }
        }
    }

    // Helper function to compute the Longest Prefix Suffix (LPS) array for KMP
    private int[] computeLPSArray(String pattern) {
        int[] lps = new int[pattern.length()];
        int length = 0;
        int i = 1;
        while (i < pattern.length()) {
            if (pattern.charAt(i) == pattern.charAt(length)) {
                length++;
                lps[i] = length;
                i++;
            } else {
                if (length != 0) {
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    // Load search frequencies from the log file at startup
    private void loadSearchFrequenciesFromLog() {
        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String word = parts[0];
                    int count = Integer.parseInt(parts[1]);
                    searchFrequencyMap.put(word, count);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading search frequencies: " + e.getMessage());
        }
    }

    // Update the log file with the current search frequencies
    public void updateLogFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath))) {
            for (Map.Entry<String, Integer> entry : searchFrequencyMap.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating the log file: " + e.getMessage());
        }
    }

    // Method to display the top 3 most searched terms
    public void displayTopSearches() {
        System.out.println("Top 3 Searches:");
        searchFrequencyMap.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(3)
                .forEach(entry -> System.out.println("Word: " + entry.getKey() + ", Frequency: " + entry.getValue()));
    }

    // Method to get the frequency of a searched word
    public int getSearchFrequency(String word) {
        return searchFrequencyMap.getOrDefault(word, 0);
    }

    // Method to load data from the CSV files
//    public void loadDataFromCSV(String... filePaths) {
//        for (String filePath : filePaths) {
//            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
//                String line;
//                while ((line = br.readLine()) != null) {
//                    data.add(line);  // Add each line to the data list
//                }
//            } catch (IOException e) {
//                System.err.println("Error reading the file: " + e.getMessage());
//            }
//        }
//    }


    // use assest manger
    public void loadDataFromCSV(Context context, String... fileNames) {
        AssetManager assetManager = context.getAssets();
        for (String fileName : fileNames) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(assetManager.open(fileName)))) {
                String line;
                while ((line = br.readLine()) != null) {
                    data.add(line);
                }
            } catch (IOException e) {
                System.err.println("Error reading the file: " + e.getMessage());
            }
        }
    }

    // Main method to demonstrate the functionality

}