package com.example.pickaplan.features.WordCompletion;

import java.util.ArrayList;
import java.util.List;

public class MinHeap {
    private String[] words;
    private int[] frequencies;
    private int size;
    private int capacity;

    public MinHeap(int capacity) {
        this.capacity = capacity;
        words = new String[capacity];
        frequencies = new int[capacity];
        size = 0;
    }

    public void insert(String word, int frequency) {
        if (size < capacity) {
            words[size] = word;
            frequencies[size] = frequency;
            size++;
            heapifyUp(size - 1);
        } else if (frequency > frequencies[0]) {
            words[0] = word;
            frequencies[0] = frequency;
            heapifyDown(0);
        }
    }

    private void heapifyUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (frequencies[index] < frequencies[parentIndex]) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    private void heapifyDown(int index) {
        while (index < size) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int smallest = index;

            if (leftChild < size && frequencies[leftChild] < frequencies[smallest]) {
                smallest = leftChild;
            }
            if (rightChild < size && frequencies[rightChild] < frequencies[smallest]) {
                smallest = rightChild;
            }
            if (smallest != index) {
                swap(index, smallest);
                index = smallest;
            } else {
                break;
            }
        }
    }

    private void swap(int i, int j) {
        String tempWord = words[i];
        int tempFrequency = frequencies[i];
        words[i] = words[j];
        frequencies[i] = frequencies[j];
        words[j] = tempWord;
        frequencies[j] = tempFrequency;
    }

    public List<String> getTopSuggestions() {
        List<String> topSuggestions = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            topSuggestions.add(words[i] + " (" + frequencies[i] + ")");
        }
        return topSuggestions;
    }
}
