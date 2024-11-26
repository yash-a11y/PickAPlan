package com.example.pickaplan.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.pickaplan.R;
import com.google.android.material.chip.Chip;

public class analyticsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup views, Bundle savedState)
    {
        View view =  layoutInflater.inflate(R.layout.analytics_activity,views,false);


        Chip pageRank = view.findViewById(R.id.chip_page_ranking);
        Chip searchFreq = view.findViewById(R.id.chip_search_freq);
        Chip frecount = view.findViewById(R.id.chip_frequency_count);
        Chip invertedIndexchip = view.findViewById(R.id.chip_invertedIndex);


        loadFragment(new searchFreq());

        searchFreq.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                searchFreq.setTextColor(ContextCompat.getColor(view.getContext(),R.color.primaryAccent));
                pageRank.setTextColor(ContextCompat.getColor(view.getContext(),R.color.white));
                frecount.setTextColor(ContextCompat.getColor(view.getContext(),R.color.white));
                invertedIndexchip.setTextColor(ContextCompat.getColor(view.getContext(),R.color.white));

                loadFragment(new searchFreq());
            }
        });

        pageRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                searchFreq.setTextColor(ContextCompat.getColor(view.getContext(),R.color.white));
                pageRank.setTextColor(ContextCompat.getColor(view.getContext(),R.color.primaryAccent));
                frecount.setTextColor(ContextCompat.getColor(view.getContext(),R.color.white));
                invertedIndexchip.setTextColor(ContextCompat.getColor(view.getContext(),R.color.white));
                loadFragment(new pageRanking());
            }
        });




        frecount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchFreq.setTextColor(ContextCompat.getColor(view.getContext(),R.color.white));
                pageRank.setTextColor(ContextCompat.getColor(view.getContext(),R.color.white));
                frecount.setTextColor(ContextCompat.getColor(view.getContext(),R.color.primaryAccent));
                invertedIndexchip.setTextColor(ContextCompat.getColor(view.getContext(),R.color.white));
                loadFragment(new frequencyCount());
            }
        });


        invertedIndexchip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFreq.setTextColor(ContextCompat.getColor(view.getContext(),R.color.white));
                pageRank.setTextColor(ContextCompat.getColor(view.getContext(),R.color.white));
                frecount.setTextColor(ContextCompat.getColor(view.getContext(),R.color.white));
                invertedIndexchip.setTextColor(ContextCompat.getColor(view.getContext(),R.color.primaryAccent));
                loadFragment(new invertedIndex());
            }
        });

        return view;

    }


    private void loadFragment(Fragment fragment) {
        if(fragment != null)
        {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .commit();
        }
    }

}
