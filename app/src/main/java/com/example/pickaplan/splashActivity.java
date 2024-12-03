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
            // Check if user is logged in
            if (isUserLoggedIn()) {
                // If the user is logged in, navigate to HomeActivity
                Intent intent = new Intent(splashActivity.this, HomeActivity.class);
                startActivity(intent);
            } else {
                // If the user is not logged in, navigate to SignUpActivity
                Intent intent = new Intent(splashActivity.this, signUp.class);
                startActivity(intent);
            }
            finish(); // Close splash activity
        }, 6000); // Wait for 6 seconds before transitioning
    }

    // Method to check if the user is logged in (using SharedPreferences or other session management)
    private boolean isUserLoggedIn() {
        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        // Check if user token or session exists
        String userToken = preferences.getString("user_token", null);
        return userToken != null; // Return true if user is logged in, false otherwise
    }
}
