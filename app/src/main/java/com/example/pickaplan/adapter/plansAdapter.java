package com.example.pickaplan.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickaplan.R;
import com.example.pickaplan.dataClass.planData;
import com.example.pickaplan.payment.StripePaymentHandler;
import com.stripe.android.PaymentConfiguration;

import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.util.List;

public class plansAdapter extends RecyclerView.Adapter<myViewholder> {
    private static final String TAG = "plansAdapter";
    private Context context;
    private List<planData> plans;
    private int operator;
    private StripePaymentHandler stripePaymentHandler;
    private static final String BACKEND_URL = "http://10.0.2.2:8080/api/stripe";
    private static final String STRIPE_PUBLISHABLE_KEY = "pk_test_51QxEhMP3BBw0AfvneJ3JHHhvrGZyvO8Qg4Tcb0M8TnxHFPbfktOe7AwDnZqbHTd6mNyM60HZMsHd89oxzfNv8zuW004SAIoPJw";

    public plansAdapter(Context context, List<planData> plans, int operator) {
        this.plans = plans;
        this.context = context;
        this.operator = operator;

        if (context instanceof AppCompatActivity) {
            try {
                PaymentConfiguration.init(context, STRIPE_PUBLISHABLE_KEY);
                this.stripePaymentHandler = new StripePaymentHandler((AppCompatActivity) context);
            } catch (Exception e) {
                Log.e(TAG, "Error initializing Stripe: " + e.getMessage());
                Toast.makeText(context, "Error initializing payment system", Toast.LENGTH_SHORT).show();
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

        // Add click listener for the buy button
        holder.itemView.setOnClickListener(v -> startPayment(plan));
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    public void updateData(List<planData> newItems) {
        plans.clear();
        plans.addAll(newItems);
        notifyDataSetChanged();  // Refreshes the entire RecyclerView
    }

    private void startPayment(planData plan) {
        // Add this log at the start
        Log.d(TAG, "Starting payment process for plan: " + plan.getPlanName());

        if (stripePaymentHandler == null) {
            Log.e(TAG, "Payment handler is null!");
            Toast.makeText(context, "Payment system not available", Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .build();

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        JSONObject requestJson = new JSONObject();
        try {
            String priceStr = plan.getPrice().replace("$", "").trim();
            double priceAmount = Double.parseDouble(priceStr) * 100;

            requestJson.put("amount", (int)priceAmount);
            requestJson.put("currency", "CAD");
            requestJson.put("description", "Payment for " + plan.getPlanName());

            // Add this log
            Log.d(TAG, "Full request URL: " + BACKEND_URL + "/create-payment-intent");
            Log.d(TAG, "Full request body: " + requestJson.toString());

        } catch (Exception e) {
            Log.e(TAG, "Error creating payment request: " + e.getMessage(), e);
            showToast("Error creating payment request: " + e.getMessage());
            return;
        }

        RequestBody body = RequestBody.create(mediaType, requestJson.toString());
        Request request = new Request.Builder()
                .url(BACKEND_URL + "/create-payment-intent")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();

        // Add this log
        Log.d(TAG, "Sending request with headers: " + request.headers().toString());

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "Network error: " + e.getMessage(), e);
                Log.e(TAG, "Failed request URL: " + call.request().url());
                showToast("Network error: Please check your internet connection");
            }

            // In your startPayment method, update the response handling:
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseBody = response.body() != null ? response.body().string() : "No response body";
                Log.d(TAG, "Response code: " + response.code());
                Log.d(TAG, "Response body: " + responseBody);

                if (!response.isSuccessful()) {
                    Log.e(TAG, "Server error: " + response.code() + ", Body: " + responseBody);
                    showToast("Server error " + response.code() + ": Please try again later");
                    return;
                }

                try {
                    JSONObject responseJson = new JSONObject(responseBody);
                    String clientSecret = responseJson.getString("clientSecret");

                    if (context instanceof AppCompatActivity) {
                        ((AppCompatActivity) context).runOnUiThread(() -> {
                            try {
                                stripePaymentHandler.handleServerResponse(clientSecret);
                            } catch (Exception e) {
                                Log.e(TAG, "Error presenting payment sheet: " + e.getMessage(), e);
                                showToast("Error processing payment. Please try again.");
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error processing response: " + e.getMessage(), e);
                    showToast("Error processing server response. Please try again.");
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
}




