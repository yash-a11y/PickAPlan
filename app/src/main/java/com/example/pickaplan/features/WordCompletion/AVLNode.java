package com.example.pickaplan.features.WordCompletion;

class AVLNode {
    String word;
    int frequency;
    AVLNode left, right;
    int height;

    AVLNode(String word) {
        this.word = word;
        this.frequency = 1;
        this.height = 1;
    }
}
