package com.example.pickaplan.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
import androidx.fragment.app.Fragment;

import com.example.pickaplan.Plans;
import com.example.pickaplan.R;
import com.example.pickaplan.adapter.gridAdapter;

import java.util.ArrayList;
import java.util.List;

public class BrandActivity extends Fragment {


    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup views, Bundle savedState){

        View view = layoutInflater.inflate(R.layout.brand_fragment,views,false);

        List<Integer> photouri = new ArrayList<>();
        photouri.add(
                R.drawable.fido
        );
        photouri.add(
                R.drawable.rogers
        );
        photouri.add(
                R.drawable.telus
        );
        photouri.add(
                R.drawable.koodo
        );



        GridView gridView = view.findViewById(R.id.grid_view);

        gridAdapter adapter = new gridAdapter(getActivity(),photouri);

        gridView.setAdapter(adapter);


        return view;
    }
}
