package com.example.pickaplan.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickaplan.R;
import com.example.pickaplan.dataClass.planData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import com.example.pickaplan.payment.StripePaymentHandler;
import okhttp3.*;
import java.io.IOException;
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

        // Check if the plan is liked
        likedCommonPlans(context, plan, isLiked -> {
            if (isLiked) {
                holder.likeplans.setVisibility(View.GONE);
            } else {
                holder.likeplans.setOnClickListener(v -> {
                    Toast.makeText(context, plan.getPlanName() + " Liked!", Toast.LENGTH_SHORT).show();
                    plan.setBrand(operator);
                    saveLikedPlan(plan);
                    golbLiked((plan.getPlanName().trim().toLowerCase() + " " + plan.getPrice().trim().toLowerCase()));
                    holder.likeplans.setVisibility(View.GONE);
                });
            }
        });



        Log.d("plansAdapter", "Binding plan at position: " + position + ", Name: " + plan.getPlanName());

        // Set operator-specific image
        switch (operator) {
            case 0: holder.brandImg.setImageResource(R.drawable.fido); break;
            case 1: holder.brandImg.setImageResource(R.drawable.rogers); break;
            case 2: holder.brandImg.setImageResource(R.drawable.telus); break;
            case 3: holder.brandImg.setImageResource(R.drawable.koodo); break;
            case 4: holder.brandImg.setImageResource(R.drawable.virgin); break;
        }

        // Set other plan details
        holder.planName.setText(plan.getPlanName());
        holder.price.setText("$" + plan.getPrice());
        holder.details.setText(plan.getDetails());
        holder.itemView.setOnClickListener(v -> {

            startPayment(plan);
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
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        // Add the plan ID to the user's likedPlans
        databaseRef.child("Users").child(userId).child("Plans").child(plan.getPlanName() + " " + plan.getPrice())
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
        notifyDataSetChanged();  // Refreshes the entire RecyclerView
    }

    public static void likedCommonPlans(Context context, planData plan, FirebasePlanCheckListener listener) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isPresent = false;
                if (dataSnapshot.exists()) {
                    DataSnapshot plansSnapshot = dataSnapshot.child("Plans");
                    if (plansSnapshot.exists()) {
                        Map<String, Boolean> plans = (Map<String, Boolean>) plansSnapshot.getValue();
                        if (plans != null) {
                            for (Map.Entry<String, Boolean> entry : plans.entrySet()) {
                                String planName = entry.getKey();
                                if (planName.trim().equalsIgnoreCase(plan.getPlanName().trim() + " " + plan.getPrice().trim())) {
                                    isPresent = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                listener.onPlanChecked(isPresent); // Pass the result to the listener
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DatabaseError", "Error: " + databaseError.getMessage());
                listener.onPlanChecked(false); // Pass false in case of error
            }
        });
    }

    public static void golbLiked(String planstr) {
        DatabaseReference planRef = FirebaseDatabase.getInstance().getReference("Plans_");

        // Retrieve all plans from Firebase
        planRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Check if there are any plans


                if (dataSnapshot.exists()) {
                    // Iterate through all plans
                    for (DataSnapshot planSnapshot : dataSnapshot.getChildren()) {
                        // Get plan data for each plan
                      //  planData plan = planSnapshot.getValue(planData.class);



                        planData plan = new planData();
                        if (planSnapshot.child("brand").exists()) {
                            plan.setBrand(planSnapshot.child("brand").getValue(Integer.class));
                        }
                        if (planSnapshot.child("planname").exists()) {
                            plan.setPlanName(planSnapshot.child("planname").getValue(String.class));
                        }
                        if (planSnapshot.child("planprice").exists()) {
                            plan.setPlanPrice(planSnapshot.child("planprice").getValue(String.class));
                        }
                        if (planSnapshot.child("details").exists()) {
                            plan.setDetails(planSnapshot.child("details").getValue(String.class));
                        }
                        if (planSnapshot.child("likes").exists()) {
                            plan.setLikes(planSnapshot.child("likes").getValue(Integer.class));
                        }




                        if (plan != null) {
                            Log.d("atenter", "yes");
                            // Increment the like count (increase frequency)
                            Log.d("golbLiked", "Comparing input: " + planstr + " with plan: " + plan.getPlanName() + " " + plan.getPrice());

                            // Match planName and price to increment like frequency
                            if (planstr.trim().equalsIgnoreCase(plan.getPlanName().trim() + " " + plan.getPrice().trim())) {
                                // Handle null likes and initialize if necessary
                                int currentLikes = plan.getLikes();
                                if (currentLikes == 0) {
                                    plan.setLikes(1);  // Initialize to 1 if likes are 0 or null
                                } else {
                                    plan.setLikes(currentLikes + 1);  // Increment like count
                                }

                                // Update the plan in the Firebase database with the incremented like count
                                planRef.child(planSnapshot.getKey()).setValue(plan)
                                        .addOnSuccessListener(aVoid -> Log.d("Firebase", "Plan like frequency updated successfully"))
                                        .addOnFailureListener(e -> Log.e("Firebase", "Failed to update like frequency", e));
                            }
                        }
                    }
                } else {
                    Log.d("Firebase", "No plans found.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                Log.e("Firebase", "Failed to read plans: " + databaseError.getMessage());
            }
        });
    }

}
