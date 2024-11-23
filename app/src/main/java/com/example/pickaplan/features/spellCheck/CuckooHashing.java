package com.example.pickaplan.features.spellCheck;

import java.util.ArrayList;
import java.util.List;

public class CuckooHashing {
    private static final int TABLE_SIZE = 1009; // Here we have taken prime number to reduce the collisions
    private List<String>[] hashTable;// creating the list of string to use as hash table

    @SuppressWarnings("unchecked")
    public CuckooHashing() {
        hashTable = new ArrayList[TABLE_SIZE];// specifying the list further to be arrayList
        for (int i = 0; i < TABLE_SIZE; i++) {
            hashTable[i] = new ArrayList<>();
        }
    }

    private int hashfunction1(String key) {// first hash function
        return Math.abs(key.hashCode()) % TABLE_SIZE;
    }

    private int hashfunction2(String key) {// second hash function
        return Math.abs((key.hashCode() / TABLE_SIZE) % TABLE_SIZE);
    }

    public void insert(String key) {
        int pos1 = hashfunction1(key);
        int pos2 = hashfunction2(key);

        if (!hashTable[pos1].contains(key)) { // check if index in table 1 has the key
            hashTable[pos1].add(key);// if yes replace it if new key
        } else if (!hashTable[pos2].contains(key)) { // do the same as above with table 2 if index in table 2 has the key
            hashTable[pos2].add(key);
        }
    }

    public boolean contains(String key) {// method to check if key is in any of two hash table
        int pos1 = hashfunction1(key);
        int pos2 = hashfunction2(key);
        return hashTable[pos1].contains(key) || hashTable[pos2].contains(key);
    }
}
