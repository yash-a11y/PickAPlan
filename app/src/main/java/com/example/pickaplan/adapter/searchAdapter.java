package com.example.pickaplan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pickaplan.R;

import java.util.List;

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.searchVH> {

    private List<String> suggestions;



    public searchAdapter(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    @Override
    public searchVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pagerank_card, parent, false);
        return new searchVH(view);
    }

    @Override
    public void onBindViewHolder(searchVH holder, int position) {
        holder.suggestionText.setText(suggestions.get(position));
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    public void updateSuggestions(List<String> newSuggestions) {
        suggestions.clear();
        suggestions.addAll(newSuggestions);
        notifyDataSetChanged();
    }

    public static class searchVH extends RecyclerView.ViewHolder {

        TextView suggestionText;

        public searchVH(View itemView) {
            super(itemView);
            suggestionText = itemView.findViewById(R.id.linkTxt);
        }
    }
}
