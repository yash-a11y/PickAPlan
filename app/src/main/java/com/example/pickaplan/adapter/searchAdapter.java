package com.example.pickaplan.adapter;

import android.util.Log;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new searchVH(view);
    }

    @Override
    public void onBindViewHolder(searchVH holder, int position) {
        String wordFreq = suggestions.get(position);
        String[] contents = wordFreq.split(",");


        holder.searchWord.setText(String.valueOf(contents[0]));
        Log.d("wordv",holder.searchWord.getText().toString());
        holder.freq.setText(contents[1].trim());
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

        TextView searchWord;
        TextView freq;

        public searchVH(View itemView) {
            super(itemView);
            searchWord = itemView.findViewById(R.id.word);
            freq  = itemView.findViewById(R.id.freqV);
        }
    }
}
