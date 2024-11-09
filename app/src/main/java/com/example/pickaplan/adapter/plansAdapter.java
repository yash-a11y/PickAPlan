package com.example.pickaplan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickaplan.R;
import com.example.pickaplan.dataClass.planData;

import java.util.List;

public  class plansAdapter extends  RecyclerView.Adapter<myViewholder>{

    private Context context;
    private List<planData> plans;
    private int operator;

    public plansAdapter(Context context, List<planData> plans, int operator){
        this.plans = plans;
        this.context = context;
        this.operator = operator;
    }

    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.item_mobileplans,parent,false);
        return new myViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewholder holder, int position) {

        planData plan = plans.get(position);

        switch (operator)
        {
            case 0: {
                holder.brandImg.setImageResource(R.drawable.fido);
            }
            break;
            case 1: {
                holder.brandImg.setImageResource(R.drawable.rogers);
            }
        }

        holder.planName.setText(plan.getPlanName());
        holder.price.setText("$"+plan.getPrice());
        holder.details.setText(plan.getDetails());
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }


}


