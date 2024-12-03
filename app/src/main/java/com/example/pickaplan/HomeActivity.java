package com.example.pickaplan;

import static androidx.core.app.PendingIntentCompat.getActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickaplan.API.ApiService;
import com.example.pickaplan.API.RetrofitClient;
import com.example.pickaplan.adapter.PageRankAdpH;
import com.example.pickaplan.adapter.SuggestionsAdapter;
import com.example.pickaplan.adapter.gridAdapter;
import com.example.pickaplan.adapter.rankAdp;
import com.example.pickaplan.features.WordCompletion.AVLTree;
import com.example.pickaplan.features.WordCompletion.MinHeap;
import com.example.pickaplan.features.SearchFrequencyTracker;
import com.example.pickaplan.fragments.BrandActivity;
import com.example.pickaplan.fragments.analyticsFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private SearchFrequencyTracker tracker;
    private String logFilePath = "search_log.txt";
    private EditText searchTab;

    private  RecyclerView pagerankRv;

    private ProgressBar Hprogress;
    RecyclerView suggestionsRecyclerView;


    // word completion

    private ListView suggestionsListView;
    private AVLTree tree;
    private ArrayAdapter<String> adapter;

    //

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Save the search frequencies to the log file
        tracker.updateLogFile();
    }


    @Override
    protected void onCreate(Bundle savedInstantState)
    {

        super.onCreate(savedInstantState);
        setContentView(R.layout.home_activity);

        // Check if user is valid
        if (!isUserValid()) {
            // Redirect to SignUpActivity if the user is not valid
            Intent intent = new Intent(HomeActivity.this, signUp.class);
            startActivity(intent);
            finish();  // Finish the current activity to prevent the user from returning to it
            return;  // Ensure the rest of onCreate doesn't run
        }


        loadFragment(new BrandActivity());

        searchTab = findViewById(R.id.search_bar);
        ImageView back_button = findViewById(R.id.back_button);
        LinearLayout homenav = findViewById(R.id.nav_home);
        Hprogress = findViewById(R.id.HprogressBar);
        LinearLayout analyticnav = findViewById(R.id.nav_explore);
        LinearLayout profilenav = findViewById(R.id.nav_account);
        suggestionsRecyclerView = findViewById(R.id.suggestionsRecyclerView);
        pagerankRv = findViewById(R.id.PagerankRV);
        pagerankRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        tracker = new SearchFrequencyTracker(this);


        //word completion logic

        searchOperations();

        //


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        analyticnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tracker.updateLogFile();

                TextView title = findViewById(R.id.nav_title);
                if(title.getText() != "Analytics")
                {
                    title.setText("Analytics");
                }

                hideViewWithAnimation(Hprogress);
                hideViewWithAnimation(pagerankRv);
                hideViewWithAnimation(searchTab);
                hideViewWithAnimation(suggestionsRecyclerView);

                ImageView homeIMG = findViewById(R.id.homeimg);
                homeIMG.setImageResource(R.drawable.home);
                ImageView analysisIMG = findViewById(R.id.analysisimg);
                analysisIMG.setImageResource(R.drawable.green_analysis);

                loadFragment(new analyticsFragment());
            }
        });

        homenav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView title = findViewById(R.id.nav_title);
                if(title.getText() != "Home")
                {
                    title.setText("Home");
                }



                //showViewWithAnimation(Hprogress);
                showViewWithAnimation(pagerankRv);
                showViewWithAnimation(searchTab);
                showViewWithAnimation(suggestionsRecyclerView);

                ImageView homeIMG = findViewById(R.id.homeimg);
                homeIMG.setImageResource(R.drawable.green_home);
                ImageView analysisIMG = findViewById(R.id.analysisimg);
                analysisIMG.setImageResource(R.drawable.analytics);

                loadFragment(new BrandActivity());
            }
        });

        profilenav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, Register.class);
                startActivity(intent);
            }
        });

    }

    // Method to check if the user is valid (You can adapt this method as per your user session management)
    private boolean isUserValid() {
        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        // Replace "user_token" with the actual key you use to store the session token or login status
        String userToken = preferences.getString("user_token", null);
        return userToken != null;  // Check if the user is logged in by verifying the token
    }


    private void loadFragment(Fragment fragment) {
        if(fragment != null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            android.R.anim.fade_in,    // Enter animation
                            android.R.anim.fade_out,   // Exit animation
                            android.R.anim.fade_in,    // Pop enter animation (back stack)
                            android.R.anim.fade_out    // Pop exit animation (back stack)
                    )
                    .replace(R.id.fragment_container_home,fragment)
                    .commit();
        }
    }




    private void searchOperations() {

        AssetManager assetManager = getAssets();
        tree = new AVLTree();
        pagerankRv.setVisibility(View.VISIBLE);
        // Set up RecyclerView with LinearLayoutManager
        suggestionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        SuggestionsAdapter suggestionsAdapter = new SuggestionsAdapter(new ArrayList<>());
        suggestionsRecyclerView.setAdapter(suggestionsAdapter);




        searchTab.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = searchTab.getText().toString().trim();
                if (!query.isEmpty()) {

                    tracker.search(query.toString());
                    tracker.updateLogFile();
                    findKeyword(query,this);

                    searchTab.setText("");
                }
                return true;


            }
            return false;

        });

        searchTab.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pagerankRv.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        searchTab.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String input = s.toString().toLowerCase();
//                if (input.isEmpty()) {
//                    suggestionsRecyclerView.setVisibility(View.GONE); // Hide suggestions RecyclerView
//                    suggestionsAdapter.updateSuggestions(new ArrayList<>()); // Clear the suggestions
//                } else {
//
//
//                    List<String> suggestions = new ArrayList<>();
//                    tree.autocomplete(input, suggestions);
//
//                    // Sort and limit suggestions using MinHeap
//                    MinHeap minHeap = new MinHeap(10);  // Show top 10 suggestions
//                    for (String suggestion : suggestions) {
//                        String[] parts = suggestion.split(" \\(");
//                        String word = parts[0];
//                        int frequency = Integer.parseInt(parts[1].replace(")", ""));
//                        minHeap.insert(word, frequency);
//                    }
//
//                    List<String> topSuggestions = minHeap.getTopSuggestions();
//                    Log.d("top", topSuggestions.toString());
//
//                    // Update the adapter with the new suggestions
//                    suggestionsAdapter.updateSuggestions(topSuggestions);
//
//                    // Make the RecyclerView visible if it has suggestions
//                    if (!topSuggestions.isEmpty()) {
//                        suggestionsRecyclerView.setVisibility(View.VISIBLE);
//                    } else {
//                        suggestionsRecyclerView.setVisibility(View.GONE); // Hide if no suggestions
//                    }
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//
//        });
    }



    public void buildTree(String fileName, AVLTree tree) {
        AssetManager assetManager = getAssets();

        try (InputStream inputStream = assetManager.open(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                // Process each line and insert it into the AVL tree
                String[] words = line.split(",");
                for (String word : words) {
                    Log.d("word",word);
                    tree.insert(word.toLowerCase());
                }
            }

            System.out.println("Finished building tree with " + fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void findKeyword(String query, Context context) {
        // Show the ProgressBar
        Hprogress.setVisibility(View.VISIBLE);
        pagerankRv.setVisibility(View.GONE);

        ApiService apiService = RetrofitClient.getApiService();
        Call<List<String>> call = apiService.getRanking(query);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> linksCall, Response<List<String>> response) {
                // Hide the ProgressBar
                Hprogress.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    pagerankRv.setVisibility(View.VISIBLE);
                    List<String> rankingLinks = response.body();
                    for (String eachRank : rankingLinks) {
                        Log.d("rank", eachRank);
                    }

                    PageRankAdpH rankAdapter = new PageRankAdpH(rankingLinks);
                    pagerankRv.setAdapter(rankAdapter);
                } else {
                    // Handle unsuccessful response
                    showErrorMessage("Failed to fetch data");
                }
            }

            @Override
            public void onFailure(Call<List<String>> linkCall, Throwable t) {
                // Hide the ProgressBar
                Hprogress.setVisibility(View.GONE);
                pagerankRv.setVisibility(View.GONE);
                showErrorMessage("Network error: " + t.getMessage());
                Log.d("fail", t.getMessage());
            }
        });
    }

    private void showErrorMessage(String message) {
        // Implement this method to show an error message to the user
        // For example, you could use a Toast or a TextView
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void hideViewWithAnimation(View view) {
        view.animate()
                .alpha(0f) // Fade out
                .setDuration(300) // Duration in ms
                .withEndAction(() -> view.setVisibility(View.GONE)) // Set GONE after animation
                .start();
    }

    private void showViewWithAnimation(View view) {
        view.setVisibility(View.VISIBLE); // Make visible first
        view.setAlpha(0f); // Initially invisible
        view.animate()
                .alpha(1f) // Fade in
                .setDuration(300) // Duration in ms
                .start();
    }

}