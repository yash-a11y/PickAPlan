package com.example.pickaplan.adapter;



import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pickaplan.R;
import com.example.pickaplan.dataClass.planData;

import java.util.List;

public class PageRankAdpH extends RecyclerView.Adapter<PageRankAdpH.searchVH> {

    private  final String[] links = {
            "https://www.fido.ca/phones/bring-your-own-device?icid=F_WIR_CNV_GRM6LG&flowType=byod",
            "https://www.rogers.com/plans?icid=R_WIR_CMH_6WMCMZ",
            "https://www.virginplus.ca/en/plans/postpaid.html#!/?rate=BYOP",
            "https://www.telus.com/en/mobility/plans"
    };
    public interface OnItemClickListener {
        void onItemClick(String suggest);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    private List<String> suggestions;



    public PageRankAdpH(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    @Override
    public searchVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pagerank_item, parent, false);
        return new searchVH(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(searchVH holder, int position) {
        String wordFreq = suggestions.get(position);

        String[] contents = wordFreq.split(",");
        Log.d("wordfr",contents[0]);

        if(contents[0].contains(links[0]))
        {
            holder.searchWord.setText("fido");
        }
        else if(contents[0].contains(links[1]))
        {
            holder.searchWord.setText("rogers");
        }
        else if(contents[0].contains(links[2]))
        {
            holder.searchWord.setText("virgin");
        }
        else
        {
            holder.searchWord.setText("telus");
        }

        Log.d("wordv",holder.searchWord.getText().toString());
        holder.freq.setText(contents[1].trim());

        holder.PageRankClick.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(contents[0]);
            }
        });
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
        LinearLayout PageRankClick;

        public searchVH(View itemView) {
            super(itemView);

            PageRankClick = itemView.findViewById(R.id.PageRankClick);
            searchWord = itemView.findViewById(R.id.rankword);
            freq  = itemView.findViewById(R.id.freqV);
        }
    }
}
