package com.example.pickaplan.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.pickaplan.R;
import com.google.android.material.chip.Chip;

public class RecomendFrag extends Fragment {

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup views, Bundle savedState)
    {
        View view =  layoutInflater.inflate(R.layout.recomendation_activity,views,false);


        Chip suggestedPlans = view.findViewById(R.id.chip_top_liked);
     //   Chip myLikedPlans = view.findViewById(R.id.chip_myLiked);



        loadFragment(new topSuggested());

        suggestedPlans.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                suggestedPlans.setTextColor(ContextCompat.getColor(view.getContext(),R.color.primaryAccent));



                loadFragment(new topSuggested());
            }
        });

//        myLikedPlans.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                myLikedPlans.setTextColor(ContextCompat.getColor(view.getContext(),R.color.white));
//                suggestedPlans.setTextColor(ContextCompat.getColor(view.getContext(),R.color.primaryAccent));
//
//                loadFragment(new topSuggested());
//            }
//        });





        return view;

    }


    private void loadFragment(Fragment fragment) {
        if(fragment != null) {
            getChildFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_an, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
