package com.example.pickaplan;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickaplan.adapter.plansAdapter;
import com.example.pickaplan.dataClass.planData;
import com.example.pickaplan.fragments.BrandActivity;
import com.example.pickaplan.fragments.analyticsFragment;

import java.util.ArrayList;
import java.util.List;

public class Plans extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);

        RecyclerView plans = findViewById(R.id.plan_view);
        List<planData> list = new  ArrayList<>();
        plans.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));

        list.add(new planData(R.drawable.fido,"Fido Essential","30",
                "\"Data\\n3 GB, 30 Days Validity\\nTalk  Text\\nUnlimited Canada-Wide\\nExtras\\nWi-Fi Calling, Text Internationally"));
        list.add(new planData(R.drawable.fido,"Fido Essential","30",
                "\"Data\\n3 GB, 30 Days Validity\\nTalk  Text\\nUnlimited Canada-Wide\\nExtras\\nWi-Fi Calling, Text Internationally"));

        list.add(new planData(R.drawable.fido,"Fido Essential","30",
                "\"Data\\n3 GB, 30 Days Validity\\nTalk  Text\\nUnlimited Canada-Wide\\nExtras\\nWi-Fi Calling, Text Internationally"));

        list.add(new planData(R.drawable.fido,"Fido Essential","30",
                "\"Data\\n3 GB, 30 Days Validity\\nTalk  Text\\nUnlimited Canada-Wide\\nExtras\\nWi-Fi Calling, Text Internationally"));

        list.add(new planData(R.drawable.fido,"Fido Essential","30",
                "\"Data\\n3 GB, 30 Days Validity\\nTalk  Text\\nUnlimited Canada-Wide\\nExtras\\nWi-Fi Calling, Text Internationally"));

        plansAdapter adpater = new plansAdapter(this,list);

        plans.setAdapter(adpater);

        LinearLayout homeNav = findViewById(R.id.nav_home);
        LinearLayout  analysisNav = findViewById(R.id.nav_explore);

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

    private void loadFragment(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,fragment)
                .commit();
    }
}