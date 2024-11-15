package com.example.pickaplan.features;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class WordFrequencyCounter {
    private Map<String, Integer> wordCountMap = new HashMap<>();


    // Method to parse the text from a single file and calculate word frequencies
    public void parseFile(Context context, String logFileName) {
        File file = new File(context.getExternalFilesDir(null), logFileName);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split on spaces or any non-word characters
                String[] words = line.toLowerCase().split("\\s+|\\W+");
                for (String word : words) {
                    if (!word.isEmpty()) {
                        wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to get all words sorted by frequency
    public List<Map.Entry<String, Integer>> getSortedWordFrequencies() {
        PriorityQueue<Map.Entry<String, Integer>> maxHeap = new PriorityQueue<>(
                (a, b) -> b.getValue().compareTo(a.getValue())
        );
        maxHeap.addAll(wordCountMap.entrySet());

        List<Map.Entry<String, Integer>> sortedWords = new ArrayList<>();
        while (!maxHeap.isEmpty()) {
            sortedWords.add(maxHeap.poll());
        }
        return sortedWords;
    }

//    // Main method to run the program
//    public static void main(String[] args) {
//        WordFrequencyCounter counter = new WordFrequencyCounter();
//
//        // Add file paths to parse
//        List<String> filePaths = Arrays.asList("fido.csv", "rogers.csv"); // Add more files as needed
//        counter.parseFiles(filePaths);
//
//        // Get sorted word frequencies and print
//        List<Map.Entry<String, Integer>> sortedWords = counter.getSortedWordFrequencies();
//        for (Map.Entry<String, Integer> entry : sortedWords) {
//            System.out.println(entry.getKey() + " : " + entry.getValue());
//        }
//    }
}