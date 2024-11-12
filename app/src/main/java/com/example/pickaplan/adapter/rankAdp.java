package com.example.pickaplan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickaplan.R;

import java.util.List;

public class rankAdp extends RecyclerView.Adapter<rankVH> {

    private Context context;
    private List<String> links;

    public rankAdp(Context context, List<String> links){
        this.links = links;
        this.context = context;

    }

    @NonNull
    @Override
    public rankVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.pagerank_card,parent,false);
        return new rankVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull rankVH holder, int position) {

        String link = links.get(position);

        holder.linkRank.setText(link);

    }

    @Override
    public int getItemCount() {
        return links.size();
    }


}
