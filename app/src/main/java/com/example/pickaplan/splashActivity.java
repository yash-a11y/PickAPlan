package com.example.pickaplan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class splashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        ImageView splashImg = findViewById(R.id.splash_image);

        Animation anim = AnimationUtils.loadAnimation(this,R.anim.animation);

        splashImg.setVisibility(ImageView.VISIBLE);
        splashImg.startAnimation(anim);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(splashActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }
}
