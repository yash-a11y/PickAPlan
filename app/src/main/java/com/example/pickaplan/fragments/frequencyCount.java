package com.example.pickaplan.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pickaplan.R;
import com.google.android.material.chip.Chip;

public class frequencyCount extends Fragment {

    private Chip frefido, freRogers;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup views, Bundle savedState) {

        View view = layoutInflater.inflate(R.layout.frequncycount, views, false);

        frefido = view.findViewById(R.id.chip_fido);

        frefido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileName="fido.csv";
                loadFragment(new operatorfrecount(fileName));
            }
        });

        freRogers = view.findViewById(R.id.chip_rogers);

        freRogers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileName="rogers.csv";
                loadFragment(new operatorfrecount(fileName));
            }
        });

        return view;
    }

    private void loadFragment(Fragment fragment) {
        if(fragment != null)
        {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_operator,fragment)
                    .commit();
        }
    }

}
