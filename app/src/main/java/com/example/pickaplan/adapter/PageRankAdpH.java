package com.example.pickaplan.adapter;



import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickaplan.Plans;
import com.example.pickaplan.R;
import com.example.pickaplan.dataClass.planData;
import com.example.pickaplan.dataClass.rankedPlan;

import java.util.ArrayList;
import java.util.List;

public class PageRankAdpH extends RecyclerView.Adapter<PageRankAdpH.searchVH> {

    private List<rankedPlan> rankedPlans;
    private Context context;

    public PageRankAdpH(List<rankedPlan> rankedPlans, Context context)
    {
        this.rankedPlans = rankedPlans;
        Log.d("const", String.valueOf(rankedPlans.size()));
        this.context = context;
    }
    @NonNull
    @Override
    public searchVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("adp","done");
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.pagerank_item,parent,false);
        return new searchVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull searchVH holder, int position) {

        Log.d("onb", "working");

        rankedPlan eachPlan = rankedPlans.get(position);

        Log.d("rankall", eachPlan.toString());


        switch (eachPlan.getOperator())
        {
            case 0:{
                holder.searchWord.setText("fido");
            }
            break;
            case 1:{
                holder.searchWord.setText("rogers");
            }
            break;
            case 2:{
                holder.searchWord.setText("telus");
            }
            break;
            case 3:{
                holder.searchWord.setText("koodo");
            }
            break;
            case 4:{
                holder.searchWord.setText("virgin");
            }
            break;

        }


        holder.freq.setText(String.valueOf(eachPlan.getFreq()));

        holder.PageRankClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Plans.class);
                intent.putExtra("planData", (ArrayList<planData>) eachPlan.getPlans());
                intent.putExtra("opr", eachPlan.getOperator());
                context.startActivity(intent);

            }
        });


    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateSuggestions(List<rankedPlan> newPlans) {
        Log.d("updatein", "yes");
        if (newPlans != null && !newPlans.isEmpty()) {
            rankedPlans.clear();
            rankedPlans.addAll(newPlans);
            notifyDataSetChanged(); // Notifying the RecyclerView to refresh the view
        } else if (newPlans.isEmpty()){
            rankedPlans.clear();
            rankedPlans.addAll(newPlans);
            notifyDataSetChanged();
        }
            else {
            Log.d("updatein", "No new data to update");
        }
    }
    @Override
    public int getItemCount() {
        return rankedPlans.size();
    }

    public static class searchVH extends RecyclerView.ViewHolder {

        TextView searchWord;
        TextView freq;
       LinearLayout PageRankClick;

        public searchVH(View itemView) {
            super(itemView);

            PageRankClick = itemView.findViewById(R.id.PageRankClick);
            searchWord = itemView.findViewById(R.id.rankword);
            freq  = itemView.findViewById(R.id.freqV);
        }
    }
}
