package com.example.pickaplan;

import static androidx.core.app.PendingIntentCompat.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.pickaplan.adapter.gridAdapter;
import com.example.pickaplan.features.SearchFrequencyTracker;
import com.example.pickaplan.fragments.BrandActivity;
import com.example.pickaplan.fragments.analyticsFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private SearchFrequencyTracker tracker;
    private String logFilePath = "search_log.txt";

    @Override
    protected void onCreate(Bundle savedInstantState)
    {

        super.onCreate(savedInstantState);
        setContentView(R.layout.home_activity);

        EditText searchTab = findViewById(R.id.search_bar);
        ImageView back_button = findViewById(R.id.back_button);
        LinearLayout homenav = findViewById(R.id.nav_home);
        LinearLayout analyticnav = findViewById(R.id.nav_explore);
        LinearLayout profilenav = findViewById(R.id.nav_account);

        tracker = new SearchFrequencyTracker(getFilesDir().getPath() + "/" + logFilePath);
        tracker.loadDataFromCSV(this, "web7_plans.csv");  // Adjust file paths if necessary



        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Set up listener for "Enter" key
        searchTab.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = searchTab.getText().toString().trim();
                if (!query.isEmpty()) {
                    handleSearch(query);
                }
                return true;
            }
            return false;
        });


        analyticnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView title = findViewById(R.id.nav_title);
                if(title.getText() != "Analytics")
                {
                    title.setText("Analytics");
                }

                searchTab.setVisibility(View.GONE);
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
                searchTab.setVisibility(View.VISIBLE);
                ImageView homeIMG = findViewById(R.id.homeimg);
                homeIMG.setImageResource(R.drawable.green_home);
                ImageView analysisIMG = findViewById(R.id.analysisimg);
                analysisIMG.setImageResource(R.drawable.analytics);

                loadFragment(new BrandActivity());
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


    private void handleSearch(String query) {
        tracker.search(query);  // Perform search
        int frequency = tracker.getSearchFrequency(query);  // Get search frequency

        // Update UI with search result and frequency


        // Update log file to store the latest frequency data
        tracker.updateLogFile();
    }

}
