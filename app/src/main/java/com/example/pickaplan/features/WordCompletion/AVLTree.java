package com.example.pickaplan.features.WordCompletion;



import java.util.List;

public class AVLTree {
    AVLNode root;

    public void insert(String word) {
        root = insert(root, word);
    }

    private AVLNode insert(AVLNode node, String word) {
        if (node == null) {
            return new AVLNode(word);
        }

        if (word.compareTo(node.word) < 0) {
            node.left = insert(node.left, word);
        } else if (word.compareTo(node.word) > 0) {
            node.right = insert(node.right, word);
        } else {
            node.frequency++;
            return node;  // Word already exists, just update frequency
        }

        // Update height and balance the tree
        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }

    private int height(AVLNode node) {
        return node == null ? 0 : node.height;
    }

    private int getBalance(AVLNode node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private AVLNode balance(AVLNode node) {
        int balance = getBalance(node);

        // Left Left Case
        if (balance > 1 && node.word.compareTo(node.left.word) >= 0) {
            return rotateRight(node);
        }
        // Right Right Case
        if (balance < -1 && node.word.compareTo(node.right.word) <= 0) {
            return rotateLeft(node);
        }
        // Left Right Case
        if (balance > 1 && node.word.compareTo(node.left.word) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        // Right Left Case
        if (balance < -1 && node.word.compareTo(node.right.word) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private AVLNode rotateLeft(AVLNode z) {
        AVLNode y = z.right;
        z.right = y.left;
        y.left = z;
        z.height = 1 + Math.max(height(z.left), height(z.right));
        y.height = 1 + Math.max(height(y.left), height(y.right));
        return y;
    }

    private AVLNode rotateRight(AVLNode z) {
        AVLNode y = z.left;
        z.left = y.right;
        y.right = z;
        z.height = 1 + Math.max(height(z.left), height(z.right));
        y.height = 1 + Math.max(height(y.left), height(y.right));
        return y;
    }

    public void autocomplete(String prefix, List<String> suggestions) {
        autocomplete(root, prefix, suggestions);
    }

    private void autocomplete(AVLNode node, String prefix, List<String> suggestions) {
        if (node == null) return;

        if (node.word.startsWith(prefix)) {
            suggestions.add(node.word + " (" + node.frequency + ")");
        }

        if (prefix.compareTo(node.word) < 0) {
            autocomplete(node.left, prefix, suggestions);
        }
        autocomplete(node.right, prefix, suggestions);
    }
}
