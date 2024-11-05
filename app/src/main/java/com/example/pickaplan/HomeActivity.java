package com.example.pickaplan;

import static androidx.core.app.PendingIntentCompat.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.pickaplan.adapter.gridAdapter;
import com.example.pickaplan.fragments.BrandActivity;
import com.example.pickaplan.fragments.analyticsFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstantState)
    {

        super.onCreate(savedInstantState);
        setContentView(R.layout.home_activity);

        LinearLayout homenav = findViewById(R.id.nav_home);
        LinearLayout analyticnav = findViewById(R.id.nav_explore);
        LinearLayout profilenav = findViewById(R.id.nav_account);

        analyticnav.setOnClickListener(new View.OnClickListener() {
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

        homenav.setOnClickListener(new View.OnClickListener() {
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

        profilenav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, signUp.class);
                startActivity(intent);
            }
        });

        List<Integer>  photouri = new ArrayList<>();
        photouri.add(
                R.drawable.fido
        );
        photouri.add(
                R.drawable.rogers
        );
        photouri.add(
                R.drawable.telus
        );
        photouri.add(
                R.drawable.koodo
        );





        GridView gridView = findViewById(R.id.grid_view);

        gridAdapter adapter = new gridAdapter(this,photouri);

        gridView.setAdapter(adapter);


    }

    private void loadFragment(Fragment fragment) {
        if(fragment != null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .commit();
        }
    }
}
