package com.example.pickaplan.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
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
        Log.d("plansAdapter", "Binding plan at position: " + position + ", Name: " + plan.getPlanName());

        switch (operator)
        {
            case 0: {
                holder.brandImg.setImageResource(R.drawable.fido);
            }
            break;
            case 1: {
                holder.brandImg.setImageResource(R.drawable.rogers);
            }
            break;
            case 2: {
                holder.brandImg.setImageResource(R.drawable.telus);
            }
            break;
            case 3: {
                holder.brandImg.setImageResource(R.drawable.koodo);
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

    public void updateData(List<planData> newItems) {
        plans.clear();
        plans.addAll(newItems);
        notifyDataSetChanged();  // Refreshes the entire RecyclerView
    }

}

