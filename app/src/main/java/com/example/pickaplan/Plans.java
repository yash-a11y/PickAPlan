package com.example.pickaplan;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickaplan.API.ApiService;
import com.example.pickaplan.API.RetrofitClient;
import com.example.pickaplan.adapter.SuggestionsAdapter;
import com.example.pickaplan.adapter.plansAdapter;
import com.example.pickaplan.dataClass.planData;
import com.example.pickaplan.features.SearchFrequencyTracker;
import com.example.pickaplan.features.WordCompletion.AVLTree;
import com.example.pickaplan.features.WordCompletion.MinHeap;
import com.example.pickaplan.features.patternFind;
import com.example.pickaplan.features.spellCheck.SpellChecker;
import com.example.pickaplan.fragments.BrandActivity;
import com.example.pickaplan.fragments.analyticsFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Plans extends AppCompatActivity {

    private AVLTree tree;

    private SearchFrequencyTracker tracker;
    private RecyclerView plans;
    private RecyclerView searchRV;
    private Intent intent;
    private int oprator;
    List<String> topSearch = new ArrayList<>();

    private SpellChecker spellChecker;
    private String fileName;

    private Context context = this;

    private ProgressBar progressBar;

    private EditText searchBar;

    private Spinner options;

    private TextView notFound;

    private SuggestionsAdapter searchAdp;


    private   plansAdapter adpater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);

        searchRV = findViewById(R.id.searchRV);

        options = findViewById(R.id.sortSpinner);

        notFound = findViewById(R.id.notFound);
        progressBar = findViewById(R.id.progressBar);
        ImageView back_button = findViewById(R.id.back_button);
        searchBar = findViewById(R.id.search_bar);
       plans = findViewById(R.id.plan_view);
        tracker = new SearchFrequencyTracker(this);
        //List<planData> list = new  ArrayList<>();
        plans.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        searchRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        // Fetch mobile plans from the API
        intent = getIntent();

        oprator = intent.getIntExtra("operator",0);

        progressBar.setVisibility(View.VISIBLE);
        fetchMobilePlans();
        topSearch = tracker.displayTopSearches();

//        list.add(new planData(R.drawable.fido,"Fido Essential","30",
//                "\"Data\\n3 GB, 30 Days Validity\\nTalk  Text\\nUnlimited Canada-Wide\\nExtras\\nWi-Fi Calling, Text Internationally"));
//        list.add(new planData(R.drawable.fido,"Fido Essential","30",
//                "\"Data\\n3 GB, 30 Days Validity\\nTalk  Text\\nUnlimited Canada-Wide\\nExtras\\nWi-Fi Calling, Text Internationally"));
//
//        list.add(new planData(R.drawable.fido,"Fido Essential","30",
//                "\"Data\\n3 GB, 30 Days Validity\\nTalk  Text\\nUnlimited Canada-Wide\\nExtras\\nWi-Fi Calling, Text Internationally"));
//
//        list.add(new planData(R.drawable.fido,"Fido Essential","30",
//                "\"Data\\n3 GB, 30 Days Validity\\nTalk  Text\\nUnlimited Canada-Wide\\nExtras\\nWi-Fi Calling, Text Internationally"));
//
//        list.add(new planData(R.drawable.fido,"Fido Essential","30",
//                "\"Data\\n3 GB, 30 Days Validity\\nTalk  Text\\nUnlimited Canada-Wide\\nExtras\\nWi-Fi Calling, Text Internationally"));

        if(!topSearch.isEmpty())
        {
            searchRV.setVisibility(View.VISIBLE);
            searchAdp = new SuggestionsAdapter(topSearch);
            searchRV.setAdapter(searchAdp);
        }


        LinearLayout homeNav = findViewById(R.id.nav_home);
        LinearLayout  analysisNav = findViewById(R.id.nav_explore);


        //back press
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        homeNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchBar.setVisibility(View.VISIBLE);
                searchRV.setVisibility(View.GONE);
                options.setVisibility(View.GONE);


                TextView title = findViewById(R.id.nav_title);
                if(title.getText() != "Home")
                {
                    title.setText("Home");
                }
                ImageView homeIMG = findViewById(R.id.homeimg);
                homeIMG.setImageResource(R.drawable.green_home);
                ImageView analysisIMG = findViewById(R.id.analysisimg);
                analysisIMG.setImageResource(R.drawable.analytics);

                loadFragment(new BrandActivity());
            }
        });


        analysisNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchBar.setVisibility(View.GONE);
                searchRV.setVisibility(View.GONE);
                options.setVisibility(View.GONE);
                
                TextView title = findViewById(R.id.nav_title);
                if(title.getText() != "Analytics")
                {
                    title.setText("Analytics");
                }
                ImageView homeIMG = findViewById(R.id.homeimg);
                homeIMG.setImageResource(R.drawable.home);
                ImageView analysisIMG = findViewById(R.id.analysisimg);
                analysisIMG.setImageResource(R.drawable.green_analysis);

                loadFragment(new analyticsFragment());
            }
        });

     searchOperations();
    }

    private void fetchMobilePlans() {



        ApiService apiService = RetrofitClient.getApiService();
        Call<List<planData>> call = null;


        switch (oprator){
            case 0: {


                    call =  apiService.getFidoPlans();
                    fileName = "fido.csv";
                    callApi(call);

            }
            break;
            case 1:{


                    call = apiService.getrogersPlans();
                    fileName = "rogers.csv";
                    callApi(call);


            }
            break;
            case 2:{


                call = apiService.getTelusPlan();
                fileName = "telus.csv";
                callApi(call);


            }
            break;
            case 3:{


                call = apiService.getKoodoPlan();
                fileName = "Koodo.csv";
                callApi(call);


            }
            break;
            case 4:{


                call = apiService.getVirginPlans();
                fileName = "virgin.csv";
                callApi(call);


            }

            default:Log.d("selection_err","error");
        }




    }

    private void loadFragment(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,fragment)
                .commit();
    }

    private void callApi( Call<List<planData>> call)
    {

        tree = new AVLTree();
// Attempt to load data from CSV first
        List<planData> mobilePlans = loadDataFromCSV();

// If CSV data is available, use it; otherwise, make the API call
        if (!mobilePlans.isEmpty()) {
            // Data was loaded from CSV
            Log.d("DataSource", "Data loaded from CSV.");


            //new thread

            new Thread(() -> {
                for(planData planData : mobilePlans)
                {

                    splitData(planData.getPlanName());
                    splitData(planData.getPrice());
                    splitData(planData.getDetails());


                }
            }).start();



            //

            updateRecyclerView(mobilePlans);
//            plansAdapter adpater = new plansAdapter(Plans.this, mobilePlans, oprator);
//            plans.setAdapter(adpater);  // Set up RecyclerView with loaded data
            progressBar.setVisibility(View.GONE);
        } else {


            call.enqueue(new Callback<List<planData>>() {
                @Override
                public void onResponse(Call<List<planData>> call, Response<List<planData>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<planData> mobilePlans = response.body();


                        // Log the retrieved mobile plans data
                        for (planData plan : mobilePlans) {
                            Log.d("name", "Plan Name: " + plan.getPlanName());
                            //Log.d(TAG, "Plan Type: " + plan.getPlanType());
                            Log.d("plan", "Plan Details: " + plan.getDetails());
                            Log.d("price", "Plan Price: " + plan.getPrice());
                        }


                        //new thread

                        new Thread(() -> {
                            for(planData planData : mobilePlans)
                            {

                                splitData(planData.getPlanName());
                                splitData(planData.getPrice());
                                splitData(planData.getDetails());


                            }
                        }).start();


                        //

                        updateRecyclerView(mobilePlans);
                        saveDataToCSV(mobilePlans);
                        progressBar.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(Plans.this, "Failed to load data", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<List<planData>> call, Throwable t) {
                    Toast.makeText(Plans.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("api_data", "API call failed: " + t.getMessage());
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }


    private void saveDataToCSV(List<planData> data) {
        File csvFile = new File(getExternalFilesDir(null), fileName);
        try (FileWriter writer = new FileWriter(csvFile)) {
            // Write CSV Headere
            writer.append("Field1_Field2_Field3\n");

            // Write data rows
            for (planData item : data) {
                writer.append(item.getPlanName()).append("_");
                writer.append(item.getPrice()).append("_");
                writer.append(item.getDetails()).append("\n");
            }

            writer.flush();
            Toast.makeText(this, "Data saved to CSV!", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save data!", Toast.LENGTH_SHORT).show();
        }
    }

    private List<planData> loadDataFromCSV() {
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


    //split data

    public void splitData(String line)
    {
            String[] words = line.split(" ");
            for (String word : words) {
                Log.d("word",word);
                tree.insert(word.toLowerCase());
            }

    }

    //

    //search operation

    private void searchOperations() {


        RecyclerView suggestionsRecyclerView = findViewById(R.id.suggestionsRV);

        tree = new AVLTree();

        // Set up RecyclerView with LinearLayoutManager
        suggestionsRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        SuggestionsAdapter suggestionsAdapter = new SuggestionsAdapter(new ArrayList<>());
        suggestionsRecyclerView.setAdapter(suggestionsAdapter);


        // for search frequency
        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            String rightSpelling = "";

            List<planData> planData = loadDataFromCSV();
            notFound.setVisibility(View.GONE);
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String searchTerm = searchBar.getText().toString().trim();
                if (!searchTerm.isEmpty()) {
                    tracker.search(searchTerm);

                    try {
                        spellChecker = new SpellChecker(planData);
                        rightSpelling =  spellChecker.spellcheck(this,searchTerm);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    searchBar.setText("");              // Clear the input field if needed

                    Log.d("serchterm ",searchTerm);
                }

                // keyboard logic
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
                }

                //pattern search
                patternFind patternFind =  new patternFind();

              

                    planData = patternFind.searchResults(planData,searchTerm);
                    if(planData.isEmpty()){


                        Toast.makeText(this,"No Match Found\nDid you mean "+rightSpelling+" ?",Toast.LENGTH_LONG).show();

                    }
                    else{

                        for(planData e : planData)Log.d("pdata",e.getPlanName());
                        updateRecyclerView(planData);
                        tracker.updateLogFile();
                        topSearch = tracker.displayTopSearches();
                        if(!topSearch.isEmpty()) {
                            searchRV.setVisibility(View.VISIBLE);
                            searchAdp.updateSuggestions(topSearch);
                        }
                    }





                //

                return true; // Consume the action
            }
            return false;
        });


        // for word completion
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                notFound.setVisibility(View.GONE);
                String input = s.toString().toLowerCase();
                if (input.isEmpty()) {
                    suggestionsRecyclerView.setVisibility(View.GONE); // Hide suggestions RecyclerView
                    suggestionsAdapter.updateSuggestions(new ArrayList<>()); // Clear the suggestions
                } else {



                    List<String> suggestions = new ArrayList<>();
                    tree.autocomplete(input, suggestions);

                    // Sort and limit suggestions using MinHeap
                    MinHeap minHeap = new MinHeap(10);  // Show top 10 suggestions
                    for (String suggestion : suggestions) {
                        String[] parts = suggestion.split(" \\(");
                        String word = parts[0];
                        int frequency = Integer.parseInt(parts[1].replace(")", ""));
                        minHeap.insert(word, frequency);
                    }




                    List<String> topSuggestions = minHeap.getTopSuggestions();
                    Log.d("top", topSuggestions.toString());

                    // Update the adapter with the new suggestions
                    suggestionsAdapter.updateSuggestions(topSuggestions);

                    // Make the RecyclerView visible if it has suggestions
                    if (!topSuggestions.isEmpty()) {
                        suggestionsRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        suggestionsRecyclerView.setVisibility(View.GONE); // Hide if no suggestions
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
    }


    private void updateRecyclerView(List<planData> newPlanData) {
        try {
            if (adpater == null) {
                adpater = new plansAdapter(Plans.this, newPlanData, oprator);
                plans.setAdapter(adpater);
            } else {
                adpater.updateData(newPlanData);
            }
        } catch (Exception e) {
            Log.e("UpdateError", "Error updating RecyclerView: " + e.getMessage());
            Toast.makeText(this, "Error updating data. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }


    //
}