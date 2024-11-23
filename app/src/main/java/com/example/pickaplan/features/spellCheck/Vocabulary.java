package com.example.pickaplan.features.spellCheck;

import android.util.Log;

import com.example.pickaplan.dataClass.planData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Vocabulary {
    private Set<String> words;// intializing set of String to stores the words from the combined csv file


    public Vocabulary(List<planData> data) throws IOException {//contructor to intialize the Vocabulary class
        words = new HashSet<>(); // creating the hashSet
        loadVocabulary(data);
    }

// creating the method loadVocabulary to read the all csv files and store the words as Vocabulary
    private void loadVocabulary(List<planData> data) throws IOException {

        for(planData each : data)
        {

        String[] line1 = splitData(each.getPlanName());
        String[] line2 = splitData(each.getPrice());
        String[] line3 = splitData(each.getDetails());
                    for (String name : line1) {
                        if (!name.isEmpty() && name.length() > 2) { // Filter short or empty tokens
                            words.add(name); // adding the tokens to the vocabulary
                        }
                    }

            for (String price : line2) {
                if (!price.isEmpty() && price.length() > 2) { // Filter short or empty tokens
                    words.add(price); // adding the tokens to the vocabulary
                }
            }

            for (String details : line3) {
                if (!details.isEmpty() && details.length() > 2) { // Filter short or empty tokens
                    words.add(details); // adding the tokens to the vocabulary
                }
            }

                }
            }







    public String[] splitData(String line)
    {
        String[] words = line.split(" ");
        return words;

    }


    public boolean contains(String word) {
        return words.contains(word.toLowerCase()); // check if our vocabulary contains the word given in argument
    }

    public Set<String> getWords() {// will be used to get th word to add word in hash table using cuckoo hashing

        return words;
    }
}
