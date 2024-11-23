package com.example.pickaplan.features.spellCheck;

public class EditDistance {
    public int measureEditDistance(String term1, String term2) {// method to measure the edit distance between two string or words
        int[][] twoDArray = new int[term1.length() + 1][term2.length() + 1];// using two dimensional array as twoDArray

        for (int c1 = 0; c1 <= term1.length(); c1++) {
            for (int c2 = 0; c2 <= term2.length(); c2++) {
                if (c1 == 0) {
                    twoDArray[c1][c2] = c2;// when  c1 is 0 then put the value of c2 in the cell
                } else if (c2 == 0) {
                    twoDArray[c1][c2] = c1;// when c2 is 0 then put the value of i in the cell
                } else if (term1.charAt(c1 - 1) == term2.charAt(c2 - 1)) {
                    twoDArray[c1][c2] = twoDArray[c1 - 1][c2 - 1];// take the diagonal value when condition become true
                } else {
                    twoDArray[c1][c2] = 1 + Math.min(twoDArray[c1 - 1][c2 - 1],
                            Math.min(twoDArray[c1 - 1][c2], twoDArray[c1][c2 - 1]));
                }
            }
        }
        return twoDArray[term1.length()][term2.length()];
    }
}
