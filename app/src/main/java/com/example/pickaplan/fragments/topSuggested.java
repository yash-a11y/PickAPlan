package com.example.pickaplan.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickaplan.R;
import com.example.pickaplan.adapter.LikedPlanAdp;
import com.example.pickaplan.dataClass.planData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class topSuggested extends Fragment {

    private RecyclerView topPlans;
    private List<planData> plans;
    private LikedPlanAdp likedAdp;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup views, Bundle savedState) {
        View view = layoutInflater.inflate(R.layout.pageranking, views, false);

        topPlans = view.findViewById(R.id.plan_view);
        topPlans.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

        plans = new ArrayList<>(); // Initialize empty list

        likedAdp = new LikedPlanAdp(plans); // Create adapter with empty list
        topPlans.setAdapter(likedAdp); // Set adapter to RecyclerView

        // Call golbLiked() with a callback
        golbLiked(new OnPlanDataLoadedListener() {
            @Override
            public void onPlanDataLoaded(List<planData> loadedPlans) {
                plans.clear();
                plans.addAll(loadedPlans);
                likedAdp.notifyDataSetChanged(); // Notify adapter of data change
            }
        });

        return view;
    }

    public static void golbLiked(final OnPlanDataLoadedListener listener) {
        List<planData> plans = new ArrayList<>();
        DatabaseReference planRef = FirebaseDatabase.getInstance().getReference("Plans_");

        planRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot planSnapshot : dataSnapshot.getChildren()) {
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

                        if (plan != null && plan.getLikes() > 0) {
                            plans.add(plan);
                        }
                    }

                    Collections.sort(plans, new Comparator<planData>() {
                        @Override
                        public int compare(planData plan1, planData plan2) {
                            return Integer.compare(plan2.getLikes(), plan1.getLikes());
                        }
                    });

                    if (!plans.isEmpty()) {
                        Log.d("sorted", String.valueOf(plans.get(0).getLikes()));
                    }

                    listener.onPlanDataLoaded(plans);
                } else {
                    Log.d("Firebase", "No plans found.");
                    listener.onPlanDataLoaded(new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Failed to read plans: " + databaseError.getMessage());
                listener.onPlanDataLoaded(new ArrayList<>());
            }
        });
    }

    public interface OnPlanDataLoadedListener {
        void onPlanDataLoaded(List<planData> plans);
    }




}
