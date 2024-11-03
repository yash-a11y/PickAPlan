package com.example.pickaplan.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pickaplan.R;

public class myViewholder extends RecyclerView.ViewHolder {

    ImageView brandImg;
    TextView planName;
    TextView price;
    TextView details;
    public myViewholder(View view)
    {
        super(view);

        brandImg = view.findViewById(R.id.brandimg);
        planName = view.findViewById(R.id.planTitle);
        price  = view.findViewById(R.id.planPrice);
        details = view.findViewById(R.id.tvPlanDescription);


    }
}
