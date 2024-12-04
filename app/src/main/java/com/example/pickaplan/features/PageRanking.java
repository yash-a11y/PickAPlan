package com.example.pickaplan.features;

import android.content.Context;
import android.util.Log;

import com.example.pickaplan.dataClass.planData;
import com.example.pickaplan.dataClass.rankedPlan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageRanking {

    // Class to perform Boyer-Moore keyword search
    public static class KeywordMatcher {
        private Map<Character, Integer> badCharSkip;
        private String keyword;

        public KeywordMatcher(String keyword) {
            this.keyword = keyword;
            this.badCharSkip = new HashMap<>();

            // Fill map with the rightmost position of each character in the pattern
            for (int j = 0; j < keyword.length(); j++) {
                badCharSkip.put(keyword.charAt(j), j);
            }
        }

        public int search(String text) {
            int patternLength = keyword.length();
            int textLength = text.length();
            int skip;

            for (int i = 0; i <= textLength - patternLength; i += skip) {
                skip = 0;
                for (int j = patternLength - 1; j >= 0; j--) {
                    char currentChar = text.charAt(i + j);
                    if (keyword.charAt(j) != currentChar) {
                        // Use badCharSkip.getOrDefault to handle characters not in the pattern
                        skip = Math.max(1, j - badCharSkip.getOrDefault(currentChar, -1));
                        break;
                    }
                }
                if (skip == 0) return i; // Pattern found
            }
            return textLength; // Pattern not found
        }
    }

    // Count occurrences of the keyword in the page content
    public static int countOccurrences(String text, String keyword) {
        KeywordMatcher matcher = new KeywordMatcher(keyword);
        int count = 0;
        int offset = 0;

        while (offset <= text.length() - keyword.length()) {
            int matchIndex = matcher.search(text.substring(offset));
            if (matchIndex == text.length() - offset) break;
            count++;
            offset += matchIndex + keyword.length();
        }
        return count;
    }

    // Quick Sort implementation for sorting entries by value in descending order
    public static void quickSort(List<Map.Entry<String, Integer>> list, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(list, low, high);
            quickSort(list, low, pivotIndex - 1);
            quickSort(list, pivotIndex + 1, high);
        }
    }

    // Partition method for Quick Sort
    private static int partition(List<Map.Entry<String, Integer>> list, int low, int high) {
        Map.Entry<String, Integer> pivot = list.get(high);  // pivot element
        int i = low - 1;

        for (int j = low; j < high; j++) {
            // Modify condition to sort in descending order
            if (list.get(j).getValue() > pivot.getValue()) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, high);  // Move pivot element to the correct position
        return i + 1;
    }

    // Rank Pages based on keyword frequency

    public static List<rankedPlan> rankPages(String keyword,List<List<planData>> planOfOpr) {
        Map<String, Integer> pageFrequencyMap = new HashMap<>();

        Log.d("callrank","oky");
        // Count occurrences of keyword in each page
        List<rankedPlan> addedPlans = new ArrayList<>();




        int i =0;
        for(List<planData> oprPlans : planOfOpr)
        {

            List<planData> plansData = new ArrayList<>();
            int freq = 0;
        for (planData plan : oprPlans) {


            Log.d("pdata",plan.getPlanName());
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(plan.getPlanName()
            ).append(plan.getPrice()).append(plan.getDetails());

            Log.d("finalstr", stringBuilder.toString());
            int frequency = countOccurrences(stringBuilder.toString().toLowerCase(), keyword.toLowerCase());

            Log.d("fcount", String.valueOf(frequency));
            if(frequency > 0)
            {
                freq++;
                Log.d("fcnnt", String.valueOf(freq));
                plansData.add(plan);
            }


//            // Debug: Print the frequency for each page
//            System.out.println("Page: " + pageName + " - Keyword Frequency: " + frequency);
        }


        addedPlans.add(new rankedPlan(freq,plansData,i));

        i++;

        }



        // Convert Map to List of Map entries
     //   List<Map.Entry<String, Integer>> sortedPages = new ArrayList<>(pageFrequencyMap.entrySet());

        // Use quick sort to sort the pages by frequency in descending order

        Collections.sort(addedPlans, new Comparator<rankedPlan>() {
            @Override
            public int compare(rankedPlan plan1, rankedPlan plan2) {
                // Sort in descending order (highest freq first)
                return Integer.compare(plan2.getFreq(), plan1.getFreq());
            }
        });



      //  quickSort(sortedPages, 0, sortedPages.size() - 1);

//        // Debug: Print the sorted pages
//        System.out.println("\nSorted pages after quick sort:");
//        for (Map.Entry<String, Integer> entry : sortedPages) {
//            System.out.println(entry.getKey() + ": " + entry.getValue() + " occurrences");
//        }

        // Return sorted list


        Log.d("addedp", addedPlans.get(0).toString());
        return addedPlans;
    }


    private static List<planData> loadDataFromCSV(String file,Context context) {
        List<planData> data = new ArrayList<>();
        File csvFile = new File(context.getExternalFilesDir(null), file);

        if (!csvFile.exists()){

            Log.d(
                    "nofile","file not"
            );
            return data; // Return empty list if the file doesn't exist
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            reader.readLine(); // Skip the header line
            while ((line = reader.readLine()) != null) {
                Log.d("line", line);
                String[] fields = line.split("_");
                if (fields.length == 3) {
                    planData plan = new planData(0,fields[0], fields[1], fields[2]);
                    Log.d("check",fields[1]+"\n"+fields[2]);
                    data.add(plan);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


}
