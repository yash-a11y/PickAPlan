package com.example.pickaplan;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickaplan.API.ApiService;
import com.example.pickaplan.API.RetrofitClient;
import com.example.pickaplan.adapter.plansAdapter;
import com.example.pickaplan.dataClass.planData;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Plans extends AppCompatActivity {

    private RecyclerView plans;
    private Intent intent;
    private int oprator;

    private Context context = this;

    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);

        progressBar = findViewById(R.id.progressBar);
        ImageView back_button = findViewById(R.id.back_button);
       plans = findViewById(R.id.plan_view);
        //List<planData> list = new  ArrayList<>();
        plans.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));

        // Fetch mobile plans from the API
        intent = getIntent();

        oprator = intent.getIntExtra("operator",0);

        progressBar.setVisibility(View.VISIBLE);
        fetchMobilePlans();

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

    }

    private void fetchMobilePlans() {



        ApiService apiService = RetrofitClient.getApiService();
        Call<List<planData>> call = null;
        String operator = "";

        switch (oprator){
            case 0: {


                    call =  apiService.getFidoPlans();

                    callApi(call,"fido.csv");

            }
            break;
            case 1:{

                    call = apiService.getrogersPlans();

                    callApi(call,"rogers.csv");


            }
            break;
            default:Log.d("selection_err","error");
        }




    }

    private void loadFragment(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,fragment)
                .commit();
    }

    private void callApi( Call<List<planData>> call,String fileName)
    {

// Attempt to load data from CSV first
        List<planData> mobilePlans = loadDataFromCSV(fileName);

// If CSV data is available, use it; otherwise, make the API call
        if (!mobilePlans.isEmpty()) {
            // Data was loaded from CSV
            Log.d("DataSource", "Data loaded from CSV.");
            plansAdapter adpater = new plansAdapter(Plans.this, mobilePlans, oprator);
            plans.setAdapter(adpater);  // Set up RecyclerView with loaded data
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



                        plansAdapter adpater = new plansAdapter(Plans.this, mobilePlans, oprator);

                        plans.setAdapter(adpater);

                        saveDataToCSV(mobilePlans, fileName);
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


    private void saveDataToCSV(List<planData> data,String fileName) {
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

}