package com.example.pickaplan.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickaplan.R;
import com.example.pickaplan.dataClass.planData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.json.JSONObject;
import com.example.pickaplan.payment.StripePaymentHandler;
import okhttp3.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class plansAdapter extends RecyclerView.Adapter<myViewholder> {

    private Context context;
    private List<planData> plans;
    private int operator;
    private static final String TAG = "plansAdapter";
    private static final String STRIPE_PUBLISHABLE_KEY = "YOUR_PUBLISHABLE_KEY"; // Replace with your key
    private static final String BACKEND_URL = "http://10.0.2.2:8080/api/stripe"; // For Android emulator
    private StripePaymentHandler stripePaymentHandler;

    public plansAdapter(Context context, List<planData> plans, int operator) {
        this.plans = plans;
        this.context = context;
        this.operator = operator;


        if (context instanceof AppCompatActivity) {
            try {
                stripePaymentHandler = new StripePaymentHandler((AppCompatActivity) context, STRIPE_PUBLISHABLE_KEY);
            } catch (Exception e) {
                Log.e(TAG, "Error initializing Stripe: " + e.getMessage());
            }
        }
    }

    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mobileplans, parent, false);
        return new myViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewholder holder, int position) {
        planData plan = plans.get(position);
        Log.d("plansAdapter", "Binding plan at position: " + position + ", Name: " + plan.getPlanName());

        switch (operator) {
            case 0: {
                holder.brandImg.setImageResource(R.drawable.fido);
            }
            break;
            case 1: {
                holder.brandImg.setImageResource(R.drawable.rogers);
            }
            break;
            case 2: {
                holder.brandImg.setImageResource(R.drawable.telus);
            }
            break;
            case 3: {
                holder.brandImg.setImageResource(R.drawable.koodo);
            }
            break;
            case 4: {
                holder.brandImg.setImageResource(R.drawable.virgin);
            }
        }

        holder.planName.setText(plan.getPlanName());
        holder.price.setText("$" + plan.getPrice());
        holder.details.setText(plan.getDetails());

        // Handle plan click for payment
        holder.itemView.setOnClickListener(v -> {
            startPayment(plan);
        });

        // Handle like button click
        holder.likeplans.setOnClickListener(v -> {
            Toast.makeText(context, plan.getPlanName() + " Liked!", Toast.LENGTH_SHORT).show();
            saveLikedPlan(plan);
        });
    }



    private void startPayment(planData plan) {
        if (stripePaymentHandler == null) {
            Log.e(TAG, "Payment handler is null!");
            showToast("Payment system not available");
            return;
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        JSONObject requestJson = new JSONObject();
        try {
            String priceStr = plan.getPrice().replace("$", "").trim();
            double priceAmount = Double.parseDouble(priceStr) * 100;

            requestJson.put("amount", (int)priceAmount);
            requestJson.put("currency", "CAD");
            requestJson.put("description", "Payment for " + plan.getPlanName());

            Log.d(TAG, "Payment request: " + requestJson.toString());
        } catch (Exception e) {
            Log.e(TAG, "Error creating payment request: " + e.getMessage());
            showToast("Error creating payment request");
            return;
        }

        RequestBody body = RequestBody.create(mediaType, requestJson.toString());
        Request request = new Request.Builder()
                .url(BACKEND_URL + "/create-payment-intent")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "Network error: " + e.getMessage());
                showToast("Network error: Please check your internet connection");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseBody = response.body() != null ? response.body().string() : "No response body";
                Log.d(TAG, "Server response: " + responseBody);

                if (!response.isSuccessful()) {
                    Log.e(TAG, "Server error: " + response.code());
                    showToast("Server error " + response.code() + ": Please try again later");
                    return;
                }

                try {
                    JSONObject responseJson = new JSONObject(responseBody);
                    String clientSecret = responseJson.getString("clientSecret");

                    if (context instanceof AppCompatActivity) {
                        ((AppCompatActivity) context).runOnUiThread(() -> {
                            stripePaymentHandler.handleServerResponse(clientSecret);
                        });
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error processing response: " + e.getMessage());
                    showToast("Error processing payment response");
                }
            }
        });
    }

    private void showToast(String message) {
        if (context instanceof AppCompatActivity) {
            ((AppCompatActivity) context).runOnUiThread(() ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            );
        }
    }

    private void saveLikedPlan(planData plan) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get the current user's ID
//        DatabaseReference plansId = FirebaseDatabase.getInstance().getReference("plansid");

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        // Create a unique key for each liked plan
        //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Plans");

        // Create a LikedPlan object to store in the database
        //planData likedPlan = new planData(plan.getBrand(), plan.getPlanName(), plan.getPrice(), plan.getDetails());

        // Add the plan ID to the user's likedPlans
        databaseRef.child("Users").child(userId).child("Plans").child(plan.getPlanName())
                .setValue(true)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Plan liked!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    public void updateData(List<planData> newItems) {
        this.plans = newItems;
        notifyDataSetChanged();
    }
}

