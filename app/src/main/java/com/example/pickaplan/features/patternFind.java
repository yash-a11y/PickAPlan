package com.example.pickaplan.features;

import com.example.pickaplan.dataClass.planData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class patternFind {

    public static List<planData> searchResults(List<planData> planData, String searchPattern) {
        List<planData> results = new ArrayList<>();
        Pattern pattern = Pattern.compile(searchPattern, Pattern.CASE_INSENSITIVE);


        for(planData eachData : planData)
        {
            Matcher matcher1 = pattern.matcher(eachData.getPlanName());
            Matcher matcher2  = pattern.matcher(eachData.getPrice());
            Matcher matcher3 = pattern.matcher(eachData.getDetails());
            if(matcher1.find() | matcher2.find() | matcher3.find()){
                results.add(eachData);
            }
        }

        return results;
    }

}