package com.example.pickaplan.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pickaplan.R;
import com.google.android.material.chip.Chip;

public class frequencyCount extends Fragment {

    private Chip frefido, freRogers ,freTellus , freVirgin ;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup views, Bundle savedState) {

        View view = layoutInflater.inflate(R.layout.frequncycount, views, false);

        frefido = view.findViewById(R.id.chip_fido);
        freRogers = view.findViewById(R.id.chip_rogers);
        freTellus = view.findViewById(R.id.chip_tellus);
        freVirgin = view.findViewById(R.id.chip_virgin);


        frefido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                frefido.setTextColor(ContextCompat.getColor(view.getContext(), R.color.primaryAccent));
                freRogers.setTextColor(ContextCompat.getColor(view.getContext(), R.color.white));
                freTellus.setTextColor(ContextCompat.getColor(view.getContext(), R.color.white));
                freVirgin.setTextColor(ContextCompat.getColor(view.getContext(), R.color.white));

                String fileName="fido.csv";
                loadFragment(new operatorfrecount(fileName));
            }
        });



        freRogers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                frefido.setTextColor(ContextCompat.getColor(view.getContext(), R.color.white));
                freRogers.setTextColor(ContextCompat.getColor(view.getContext(), R.color.primaryAccent));
                freTellus.setTextColor(ContextCompat.getColor(view.getContext(), R.color.white));
                freVirgin.setTextColor(ContextCompat.getColor(view.getContext(), R.color.white));

                String fileName="rogers.csv";
                loadFragment(new operatorfrecount(fileName));
            }
        });



        freTellus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                frefido.setTextColor(ContextCompat.getColor(view.getContext(), R.color.white));
                freRogers.setTextColor(ContextCompat.getColor(view.getContext(), R.color.white));
                freTellus.setTextColor(ContextCompat.getColor(view.getContext(), R.color.primaryAccent));
                freVirgin.setTextColor(ContextCompat.getColor(view.getContext(), R.color.white));

                String fileName="telus.csv";
                loadFragment(new operatorfrecount(fileName));
            }
        });



        freVirgin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                frefido.setTextColor(ContextCompat.getColor(view.getContext(), R.color.white));
                freRogers.setTextColor(ContextCompat.getColor(view.getContext(), R.color.white));
                freTellus.setTextColor(ContextCompat.getColor(view.getContext(), R.color.white));
                freVirgin.setTextColor(ContextCompat.getColor(view.getContext(), R.color.primaryAccent));

                String fileName="virgin.csv";
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
