package com.example.pickaplan;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pickaplan.API.ApiService;
import com.example.pickaplan.API.RetrofitClient;
import com.example.pickaplan.dataClass.phoneData;
import com.example.pickaplan.dataClass.planData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


//        Button register = findViewById(R.id.btnsignup);
//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, signUp.class);
//                startActivity(intent);
//            }
//        });



        // Fetch mobile plans from the API
       // fetchMobilePlans();

        // Fetch phone data from the API
        getSmartphones();

    }

    private void fetchMobilePlans() {
        ApiService apiService = RetrofitClient.getApiService();

        Call<List<planData>> call = apiService.getMobilePlans();
        call.enqueue(new Callback<List<planData>>() {
            @Override
            public void onResponse(Call<List<planData>> call, Response<List<planData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<planData> mobilePlans = response.body();
                    // Log the retrieved mobile plans data
                    for (planData plan : mobilePlans) {
                        Log.d(TAG, "Plan Name: " + plan.getPlanName());
                        //Log.d(TAG, "Plan Type: " + plan.getPlanType());
                        Log.d(TAG, "Plan Details: " + plan.getDetails());
                        Log.d(TAG, "Plan Price: " + plan.getPrice());
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Failed to load data", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<planData>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("api_data", "API call failed: " + t.getMessage());
            }
        });
    }

    private void getSmartphones() {
        ApiService apiService = RetrofitClient.getApiService();

        Call<List<phoneData>> call = apiService.getSmartphones();
        call.enqueue(new Callback<List<phoneData>>() {
            @Override
            public void onResponse(Call<List<phoneData>> call, Response<List<phoneData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<phoneData> Phone = response.body();
                    // Log the retrieved mobile plans data
                    for (phoneData phone : Phone) {
                        Log.d(TAG, "Phone Name: " + phone.getPhoneName());
                        Log.d(TAG, " return condition: " + phone.getCondition());
                        Log.d(TAG, "Phone price: " + phone.getPrice());
                        Log.d(TAG, "full Price: " + phone.getFullPrice());
                        Log.d(TAG, "imgurl"+ phone.getImageUrl());
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Failed to load data", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<phoneData>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("api_data", "API call failed: " + t.getMessage());
            }
        });
    }
}