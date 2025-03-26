package com.example.pickaplan.adapter;

// WordFrequencyAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickaplan.R;
import com.example.pickaplan.dataClass.phoneData;

import java.util.List;
import java.util.Map;

public class WordFrequencyAdapter extends RecyclerView.Adapter<WordFrequencyAdapter.ViewHolder> {

    private List<Map.Entry<String, Integer>> wordFrequencyList;

    // Adapter constructor, receives the list of word-frequency data
    public WordFrequencyAdapter(List<Map.Entry<String, Integer>> wordFrequencyList) {
        this.wordFrequencyList = wordFrequencyList;
    }

    // Called when RecyclerView needs a new ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the item layout and create a ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new ViewHolder(view);
    }

    // Binds data to the views in the ViewHolder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the current WordFrequency object
        Map.Entry<String, Integer> entry = wordFrequencyList.get(position);

        // Set the word and frequency in the TextViews
        holder.wordTextView.setText(entry.getKey());
        holder.frequencyTextView.setText(String.valueOf(entry.getValue()));
    }

    // Returns the total number of items
    @Override
    public int getItemCount() {
        return wordFrequencyList.size();
    }

    // ViewHolder class to hold references to the views for each item
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView wordTextView;
        TextView frequencyTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            // Get references to the TextViews
            wordTextView = itemView.findViewById(R.id.word);
            frequencyTextView = itemView.findViewById(R.id.freqV);
        }
    }
}