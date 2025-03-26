package com.example.pickaplan.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickaplan.R;
import com.example.pickaplan.adapter.searchAdapter;
import com.example.pickaplan.features.SearchFrequencyTracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class searchFreq extends Fragment {


    SearchFrequencyTracker searchFreq;
    RecyclerView searchRV;



    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup views, Bundle savedState) {

        View view = layoutInflater.inflate(R.layout.search_activity, views, false);

        searchFreq = new SearchFrequencyTracker(view.getContext());

        searchRV = view.findViewById(R.id.search_freq);

        searchRV.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL,false));


        ArrayList<String> searchData= searchFreq.loadSearchFrequenciesFromLog();

        searchAdapter searchAdapter = new searchAdapter(searchData);

        searchRV.setAdapter(searchAdapter);


        return view;
    }


}
