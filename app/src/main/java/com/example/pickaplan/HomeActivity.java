package com.example.pickaplan;

import static androidx.core.app.PendingIntentCompat.getActivity;


import static com.example.pickaplan.features.PageRanking.rankPages;

import android.annotation.SuppressLint;
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
import com.example.pickaplan.adapter.plansAdapter;
import com.example.pickaplan.dataClass.planData;
import com.example.pickaplan.dataClass.rankedPlan;
import com.example.pickaplan.features.WordCompletion.AVLTree;
import com.example.pickaplan.features.SearchFrequencyTracker;
import com.example.pickaplan.fragments.BrandActivity;
import com.example.pickaplan.fragments.analyticsFragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

    private PageRankAdpH rankAdapter;
    private Context context = this;

    private ProgressBar Hprogress;
    RecyclerView suggestionsRecyclerView;


    // word completion

    private ListView suggestionsListView;
    private AVLTree tree;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("HomeActivity", "onDestroy called");
        if (tracker != null) {
            tracker.updateLogFile();
        } else {
            Log.w("HomeActivity", "Tracker is null in onDestroy");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstantState)
    {

        super.onCreate(savedInstantState);
        setContentView(R.layout.home_activity);

        if (!isUserValid()) {
            // Redi
            //        // Check if user is validrect to SignUpActivity if the user is not valid
            Intent intent = new Intent(HomeActivity.this, Login.class);
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
                updateRecyclerView(new ArrayList<>());

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
                Intent intent = new Intent(HomeActivity.this, Profile.class);
                startActivity(intent);
                pagerankRv.setVisibility(View.GONE);
            }
        });

    }

    // Method to check if the user is valid (You can adapt this method as per your user session management)
    private boolean isUserValid() {
        Log.d("gotol","go to login");

        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Replace "user_token" with the actual key you use to store the session token or login status
        String userToken = sharedPref.getString("name", null);
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

                    pagerankRv.setVisibility(View.VISIBLE);

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



        List<rankedPlan> rankedPlans
                = pageRankingData(query,context);

        if(!rankedPlans.isEmpty()){
            Hprogress.setVisibility(View.GONE);
            pagerankRv.setVisibility(View.VISIBLE);
            Log.d("workf", String.valueOf(rankedPlans.get(0).getFreq()));
            updateRecyclerView(rankedPlans);

        }


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


    private List<List<planData>> getallData()
    {
        String[] operators = {
                "fido.csv","rogers.csv","telus.csv","Koodo.csv","virgin.csv"
        };
        List<List<planData>> allData = new ArrayList<>();

        for(String each : operators)
        {
            List<planData> eachData = new ArrayList<>();
            eachData = loadDataFromCSV(each);

            allData.add(
                    eachData
            );

        }

        return allData;

    }


    private List<planData> loadDataFromCSV(String fileName) {
        List<planData> data = new ArrayList<>();
        File csvFile = new File(getExternalFilesDir(null), fileName);

        if (!csvFile.exists()) return data; // Return empty list if the file doesn't exist

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            reader.readLine(); // Skip the header line
            while ((line = reader.readLine()) != null) {
                Log.d("line", line);
                String[] fields = line.split("_");
                if (fields.length == 3) {
                    planData plan = new planData(0,fields[0], fields[1], fields[2]);
                    Log.d("check",fields[1]+"\n"+fields[2]);
                    data.add(plan);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private List<rankedPlan> pageRankingData(String keyword, Context context)
    {

        List<rankedPlan> rankedPlans= rankPages(keyword, getallData());

        Log.d("freqr", String.valueOf(rankedPlans.get(0).getFreq()));


        return rankedPlans;
    }

    private void updateRecyclerView(List<rankedPlan> newPlanData) {
        if (rankAdapter == null) {
            pagerankRv.setVisibility(View.VISIBLE);
            rankAdapter = new PageRankAdpH(newPlanData, context);
            pagerankRv.setAdapter(rankAdapter);
        } else {
            rankAdapter.updateSuggestions(newPlanData); // Update the data in the adapter
        }
        rankAdapter.notifyDataSetChanged(); // Notify the RecyclerView that data has changed
    }

}