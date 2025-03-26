package com.example.pickaplan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickaplan.R;

import java.util.ArrayList;
import java.util.List;

public class invertedIndexAdapter extends RecyclerView.Adapter<invertedIndexAdapter.ViewHolder> {
    private List<String[]> results = new ArrayList<>();

    public void updateResults(List<String> newResults) {
        results.clear();
        if (newResults != null) {
            for (String result : newResults) {
                String[] parts = result.split(":Line"); // Split into file name and line number
                if (parts.length == 2) {
                    results.add(parts); // Store file name and line number as separate strings
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inverted_index, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] result = results.get(position);
        holder.fileNameText.setText(result[0]); // File name
        holder.lineNumberText.setText("Line: " + result[1]); // Line number
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fileNameText;
        TextView lineNumberText;

        ViewHolder(View itemView) {
            super(itemView);
            fileNameText = itemView.findViewById(R.id.tv_filename);
            lineNumberText = itemView.findViewById(R.id.tv_wordPlace);
        }
    }
}
