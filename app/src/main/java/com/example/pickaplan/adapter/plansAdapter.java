package com.example.pickaplan.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickaplan.R;
import com.example.pickaplan.dataClass.planData;
import com.example.pickaplan.payment.StripePaymentHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class plansAdapter extends RecyclerView.Adapter<myViewholder> {

    private Context context;
    private List<planData> plans;
    private int operator;
    private StripePaymentHandler stripePaymentHandler;

    public plansAdapter(Context context, List<planData> plans, int operator) {
        this.plans = plans;
        this.context = context;
        this.operator = operator;
        this.stripePaymentHandler = new StripePaymentHandler(context);
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
            startStripePayment(plan);
        });

        // Handle like button click
        holder.likeplans.setOnClickListener(v -> {
            Toast.makeText(context, plan.getPlanName() + " Liked!", Toast.LENGTH_SHORT).show();
            saveLikedPlan(plan);
        });
    }

    private void startStripePayment(planData plan) {
        try {
            // Remove the "$" symbol and convert to cents
            String priceStr = plan.getPrice().replace("$", "").trim();
            double priceAmount = Double.parseDouble(priceStr) * 100; // Convert to cents
            stripePaymentHandler.startPayment(String.valueOf((int)priceAmount), "CAD");
        } catch (Exception e) {
            Toast.makeText(context, "Error starting payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
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

