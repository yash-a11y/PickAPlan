package com.example.pickaplan.features.spellCheck;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.pickaplan.dataClass.planData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SpellChecker {
    private Vocabulary vocabulary;
    private CuckooHashing hashTable;
    private  List<planData> planData;
    private EditDistance editDistance;
    private MergeSortSuggestions sorter;

    public SpellChecker( List<planData> planData) throws IOException {

        vocabulary = new Vocabulary(planData); //  initializing the Vocabulary object by list of csvFiles
        hashTable = new CuckooHashing();// instance of the CuckooHashing to store the Vocabulary for fast look up
        editDistance = new EditDistance();// to measure the edit distance
        sorter = new MergeSortSuggestions();// sorting the suggestions based on their edit distance
         this.planData = planData;
        for (String word : vocabulary.getWords()) {
            hashTable.insert(word.toLowerCase());// inserting the vocab in hashtable
        }
    }

    public boolean checkSpelling(String word) {
        return hashTable.contains(word);
    }

    public List<hint> suggestAlternatives(String word) {
        List<hint> hints = new ArrayList<>();
        int maxEditDistance = 3; // maximum allowable edit distance is 3

        for (String vocabWord : vocabulary.getWords()) {
            int distance = editDistance.measureEditDistance(word, vocabWord);// calculating the edit distance between typed word and all vocabularies

            // adding the word to vocab based on maximum allowable edit distance
            if (distance <= maxEditDistance) {
                hints.add(new hint(vocabWord, distance));
            }
        }

        // sorting the hints based on edit distance and then length
        hints.sort((s1, s2) -> {
            if (s1.distance != s2.distance) {
                return Integer.compare(s1.distance, s2.distance);
            } else {
                return Integer.compare(s1.word.length(), s2.word.length());
            }
        });

        // return top hint
        List<hint> tophints = hints.subList(0, Math.min(1, hints.size()));



        return tophints;
    }


    public  String spellcheck(Context context,String word) throws IOException {
        SpellChecker checker = new SpellChecker(planData);// passing the list to spellchecker class
        String rightSpelling = "";



        if (checker.checkSpelling(word)) {// check if the word exists in vocabulary
            Toast.makeText(context.getApplicationContext(),word + " is spelled correctly.",Toast.LENGTH_SHORT).show(); // if yes then it is correct
        } else {

            List<hint> hints = checker.suggestAlternatives(word);// checking for possible suggestions
            if(hints.isEmpty())
            {
                return rightSpelling;
            }
            else {


                rightSpelling = hints.get(0).word;
            }
        }
        return rightSpelling;
    }



}
