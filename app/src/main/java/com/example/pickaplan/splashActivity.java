package com.example.pickaplan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class splashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        ImageView splashImg = findViewById(R.id.splash_image);

        Animation anim = AnimationUtils.loadAnimation(this,R.anim.animation);

        splashImg.setVisibility(ImageView.VISIBLE);
        splashImg.startAnimation(anim);

        // Check user authentication after splash animation
        new Handler().postDelayed(() -> {
                Intent intent = new Intent(splashActivity.this, signUp.class);
                startActivity(intent);
            finish(); // Close splash activity
        }, 6000); // Wait for 6 seconds before transitioning
    }
}
