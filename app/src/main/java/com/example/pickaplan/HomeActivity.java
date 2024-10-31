package com.example.pickaplan;

import android.os.Bundle;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pickaplan.adapter.gridAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstantState)
    {

        super.onCreate(savedInstantState);
        setContentView(R.layout.home_activity);
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
}
