package com.example.pickaplan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pickaplan.R;

import java.util.List;

public class SuggestionsAdapter extends RecyclerView.Adapter<SuggestionsAdapter.SuggestionsViewHolder> {

    private List<String> suggestions;

    public SuggestionsAdapter(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    @Override
    public SuggestionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggestion_item, parent, false);
        return new SuggestionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SuggestionsViewHolder holder, int position) {
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

    public static class SuggestionsViewHolder extends RecyclerView.ViewHolder {

        TextView suggestionText;

        public SuggestionsViewHolder(View itemView) {
            super(itemView);
            suggestionText = itemView.findViewById(R.id.suggestion_text);
        }
    }
}
