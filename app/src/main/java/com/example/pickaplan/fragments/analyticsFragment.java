package com.example.pickaplan.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.pickaplan.R;

public class analyticsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup views, Bundle savedState)
    {
        return layoutInflater.inflate(R.layout.analytics_activity,views,false);
    }

}
