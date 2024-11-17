package com.example.pickaplan.fragments;

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

        searchFreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new searchFreq());
            }
        });

        pageRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pageRank.setTextColor(ContextCompat.getColor(view.getContext(), R.color.primaryAccent));
                loadFragment(new pageRanking());
            }
        });


        Chip frecount = view.findViewById(R.id.chip_frequency_count);

        frecount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new frequencyCount());
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
