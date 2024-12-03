package com.example.pickaplan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class signUp extends AppCompatActivity {

    private EditText etUserName, etPassword ;
    private TextView etRegister;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);


        etUserName = findViewById(R.id.usernameEditText);
        etPassword = findViewById(R.id.passwordEditText);
        btnLogin = findViewById(R.id.signInButton);
        etRegister = findViewById(R.id.HomeText);

        etRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signUp.this, Register.class);
                startActivity(intent);
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get stored credentials
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String storedUsername = sharedPreferences.getString("username", "");
                String storedPassword = sharedPreferences.getString("password", "");

                // Validate credentials
                String enteredUsername = etUserName.getText().toString().trim();
                String enteredPassword = etPassword.getText().toString().trim();

                if (enteredUsername.equals(storedUsername) && enteredPassword.equals(storedPassword)) {
                    // Mark user as logged in
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();

                    // Navigate to Home Activity
                    Toast.makeText(signUp.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    navigateToHome();
                } else {
                    Toast.makeText(signUp.this, "Invalid credentials. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void navigateToHome() {
        Intent intent = new Intent(signUp.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}