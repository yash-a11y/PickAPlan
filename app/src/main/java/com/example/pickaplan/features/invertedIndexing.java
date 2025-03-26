package com.example.pickaplan.features;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import java.io.*;
import java.util.*;

// Trie Node Class
class TrieNode {
    Map<Character, TrieNode> children;
    Set<String> documentIds;

    public TrieNode() {
        children = new HashMap<>();
        documentIds = new HashSet<>();
    }
}

// Trie Class for Inverted Index
class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String word, String documentId) {
        TrieNode current = root;
        for (char c : word.toLowerCase().toCharArray()) {
            current.children.putIfAbsent(c, new TrieNode());
            current = current.children.get(c);
        }
        current.documentIds.add(documentId);
    }

    public Set<String> search(String word) {
        TrieNode current = root;
        for (char c : word.toLowerCase().toCharArray()) {
            if (!current.children.containsKey(c)) {
                return Collections.emptySet(); // Word not found
            }
            current = current.children.get(c);
        }
        return current.documentIds;
    }
}

public class invertedIndexing {
    private static Trie trie = new Trie();

    public void buildIndex(Context context) {
        // Get the external files directory (e.g., /storage/emulated/0/Android/data/com.example.app/files)
        File directory = context.getExternalFilesDir(null);

        // Log the directory path for debugging
        Log.d("dir_name", "Looking for files in: " + directory.getAbsolutePath());

        if (directory == null) {
            Log.d("dir_name", "External storage directory is null.");
            return;
        }

        // Get the CSV files in the directory
        File[] csvFiles = directory.listFiles((dir, name) -> name.endsWith(".csv"));


        if (csvFiles == null || csvFiles.length == 0) {
            System.out.println("No CSV files found in the app's data folder.");
            return;
        }

        for (File file : csvFiles) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                int lineIndex = 1;

                while ((line = br.readLine()) != null) {
                    String documentId = file.getName() + ":Line" + lineIndex;

                    for (String word : line.split("\\W+")) {
                        trie.insert(word, documentId);
                    }
                    lineIndex++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<String> searchWord(String word) {
        Set<String> result = trie.search(word);

        if (result.isEmpty()) {
            return Collections.emptyList();
        } else {
            return new ArrayList<>(result);
        }
    }
}
