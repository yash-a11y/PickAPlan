package com.example.pickaplan.fragments;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickaplan.R;
import com.example.pickaplan.adapter.invertedIndexAdapter;
import com.example.pickaplan.features.invertedIndexing;

import java.util.List;

public class invertedIndex extends Fragment {

    private invertedIndexing InvertedIndexing;

    private invertedIndexAdapter adapter;

    private LinearLayout noDataTV;
    private EditText searchinverted;
    private RecyclerView invertedView;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup views, Bundle savedState) {

        View view = layoutInflater.inflate(R.layout.inverted_indexing, views, false);

        searchinverted = view.findViewById(R.id.search_bar_inverted);
        invertedView = view.findViewById(R.id.rvinverdindex);
        noDataTV = view.findViewById(R.id.noDataFound);

        invertedView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new invertedIndexAdapter();
        invertedView.setAdapter(adapter);

        // Initialize Inverted Index
        InvertedIndexing = new invertedIndexing();
        InvertedIndexing.buildIndex(requireContext());

//        searchinverted.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String query = s.toString().trim();
//                if (!query.isEmpty()) {
//                    List<String> results = invertedIndexing.searchWord(query);
//                    adapter.updateResults(results);
//                } else {
//                    adapter.updateResults(null); // Clear results
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });

        searchinverted.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // Check if the action is the "Enter" key or IME_ACTION_DONE
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                    String query = v.getText().toString().trim();
                    if (!query.isEmpty()) {
                        noDataTV.setVisibility(View.GONE);
                        List<String> results = invertedIndexing.searchWord(query);
                        if(results.isEmpty())
                        {
                            noDataTV.setVisibility(View.VISIBLE);
                            Toast.makeText(view.getContext(), "No data found !", Toast.LENGTH_SHORT).show();

                        }else{
                            adapter.updateResults(results); // Update the adapter with search results

                        }

                        }
                    else {
                        adapter.updateResults(null); // Clear results
                        noDataTV.setVisibility(View.VISIBLE);
                        Toast.makeText(view.getContext(), "No data found !", Toast.LENGTH_SHORT).show();
                    }
                    return true; // Return true to consume the event
                }
                return false; // Return false if it's not the search action
            }
        });


        return view;
    }


}
