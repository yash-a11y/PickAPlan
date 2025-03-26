package com.example.pickaplan.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickaplan.API.ApiService;
import com.example.pickaplan.API.RetrofitClient;
import com.example.pickaplan.Plans;
import com.example.pickaplan.R;
import com.example.pickaplan.adapter.plansAdapter;
import com.example.pickaplan.adapter.rankAdp;
import com.example.pickaplan.dataClass.planData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class pageRanking  extends Fragment {

    private EditText searchTab;
    private ProgressBar progressBar;
    private RecyclerView rankingView;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup views, Bundle savedState) {

        View view = layoutInflater.inflate(R.layout.pageranking, views, false);
        progressBar = view.findViewById(R.id.progressBar);
        searchTab = view.findViewById(R.id.search_bar);
        rankingView = view.findViewById(R.id.plan_view);

        rankingView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

        searchTab.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = searchTab.getText().toString().trim();
                if (!query.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    findKeyword(query,view.getContext());
                }
                return true;
            }
            return false;
        });


        return view;
    }


    // page ranking using api call
    private void findKeyword(String query, Context context) {

        ApiService apiService = RetrofitClient.getApiService();
        Call<List<String>> call = apiService.getRanking(query);

        // exception handling
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> linksCall, Response<List<String>> response) {
                // Handle successful response
                if(response.isSuccessful() && response.body() != null)
                {
                    List<String> rankingLinks = response.body();
                    for(String eachRank : rankingLinks)
                    {
                        Log.d("rank", eachRank);
                    }

                    rankAdp  rankAdapter = new rankAdp(context, rankingLinks);


                    rankingView.setAdapter(rankAdapter);

                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<String>> linkCall, Throwable t) {
                // Handle failure
                Log.d("fail",t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });


    }
}
