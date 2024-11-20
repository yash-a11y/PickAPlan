package com.example.pickaplan.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickaplan.R;
import com.example.pickaplan.adapter.WordFrequencyAdapter;
import com.example.pickaplan.dataClass.phoneData;
import com.example.pickaplan.features.WordFrequencyCounter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class operatorfrecount extends Fragment {

    private RecyclerView recyclerView;
    private WordFrequencyAdapter adapter;

    private  String file;

    public operatorfrecount(String file){
        this.file=file;

    }
    //private List<phoneData> wordFrequencyList;

    //private static final String LOG_FILE_NAME = "file.txt";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.operatorfrequencycount, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.rv_fredata);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        WordFrequencyCounter counter = new WordFrequencyCounter();
        counter.parseFile(this.getContext(), file);

//
//        // Set up adapter and attach it to RecyclerView
//        adapter = new WordFrequencyAdapter(sortedWords);
//        recyclerView.setAdapter(adapter);

        List<Map.Entry<String, Integer>> sortedWords = counter.getSortedWordFrequencies();
        WordFrequencyAdapter adapter = new WordFrequencyAdapter(sortedWords);
        recyclerView.setAdapter(adapter);

        return view;
    }

//    private void loadWordFrequencies() {
//        WordFrequencyCounter counter = new WordFrequencyCounter(getContext());
//        counter.parseFiles(LOG_FILE_NAME);
//        List<Map.Entry<String, Integer>> sortedWords = counter.getSortedWordFrequencies();
//
//        // Set up adapter and attach it to RecyclerView
//        adapter = new WordFrequencyAdapter(sortedWords);
//        recyclerView.setAdapter(adapter);
//
//    }

    // Sample data method (replace with real data source if needed)
//    private List<phoneData> getWordFrequencyData() {
//        List<phoneData> list = new ArrayList<>();
//        list.add(new phoneData("127", "wqasd","adsa","231","weqqw"));
//        list.add(new phoneData("631", "das","dsa","adas","adsas"));
//
//        // Add more data as needed
//        return list;
//    }
}
