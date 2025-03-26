package com.example.pickaplan.adapter;


import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.example.pickaplan.R;
import com.example.pickaplan.dataClass.planData;

import java.util.List;

public class LikedPlanAdp extends RecyclerView.Adapter<LikedPlanAdp.topVH>{


    private  List<planData> plandata;

    public  LikedPlanAdp(List<planData>  planData)
    {
        this.plandata = planData;
    }

    @NonNull
    @Override
    public topVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mobileplans, parent, false);
        return new topVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull topVH holder, int position) {

        planData plan = plandata.get(position);

        switch (plan.getBrand()) {
            case 0: holder.brandImg.setImageResource(R.drawable.fido); break;
            case 1: holder.brandImg.setImageResource(R.drawable.rogers); break;
            case 2: holder.brandImg.setImageResource(R.drawable.telus); break;
            case 3: holder.brandImg.setImageResource(R.drawable.koodo); break;
            case 4: holder.brandImg.setImageResource(R.drawable.virgin); break;
        }

        // Set other plan details
        holder.planName.setText(plan.getPlanName());
        holder.price.setText("$" + plan.getPrice());
        holder.details.setText(plan.getDetails());
        holder.likeplans.setVisibility(View.GONE);
        holder.likes.setVisibility(View.VISIBLE);
        holder.likes.setText(String.valueOf(plan.getLikes()));

    }


    @Override
    public int getItemCount() {
        return plandata.size();
    }

    public static class topVH extends RecyclerView.ViewHolder{

        ImageView brandImg;
        TextView planName;
        TextView price;
        TextView details;
        TextView likes;

        Button likeplans;
        public topVH(View view) {
            super(view);

            brandImg = view.findViewById(R.id.brandimg);
            planName = view.findViewById(R.id.planTitle);
            price  = view.findViewById(R.id.planPrice);
            details = view.findViewById(R.id.tvPlanDescription);
            likeplans = view.findViewById(R.id.btnLikePlan);
            likes = view.findViewById(R.id.likes);


        }
    }
}
