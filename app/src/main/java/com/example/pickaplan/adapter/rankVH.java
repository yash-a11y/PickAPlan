package com.example.pickaplan.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pickaplan.R;

public class rankVH extends RecyclerView.ViewHolder{
    TextView linkRank;
    public rankVH(View view)
    {
        super(view);

        linkRank = view.findViewById(R.id.linkTxt);


    }
}
