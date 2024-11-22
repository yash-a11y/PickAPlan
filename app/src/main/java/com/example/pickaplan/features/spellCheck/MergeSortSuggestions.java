package com.example.pickaplan.features.spellCheck;

import java.util.ArrayList;
import java.util.List;

public class MergeSortSuggestions {
    public void mergeSort(List<hint> hints) {
        if (hints.size() <= 1) return; // if there is only one word in list or no words then return.

        int mid = hints.size() / 2;

        List<hint> left = new ArrayList<>(hints.subList(0, mid)); // dividing the list in to left list and right list
        List<hint> right = new ArrayList<>(hints.subList(mid, hints.size()));

        mergeSort(left);// recursive call to the mergeSort on left list
        mergeSort(right);// recursive call to the mergeSort on right list
        merge(hints, left, right);// merging the left and right list
    }

    private void merge(List<hint> result, List<hint> left, List<hint> right) {
        int i = 0, j = 0, k = 0;
        while (i < left.size() && j < right.size()) {
            if (left.get(i).distance <= right.get(j).distance) {
                result.set(k++, left.get(i++));// if distance of left element is smaller than right then add it to result and increment the i counter
            } else {
                result.set(k++, right.get(j++));// else add right element to the result and increment the j counter
            }
        }
        while (i < left.size()) result.set(k++, left.get(i++));// if left list is not empty add the remaining elements of left list to the result
        while (j < right.size()) result.set(k++, right.get(j++));// if right list is not empty add the remaining elements of right list to result
    }
}
// defining the hint class to store hints
class hint {
    String word;
    int distance;

    public hint(String word, int distance) {
        this.word = word;
        this.distance = distance;
    }
}
