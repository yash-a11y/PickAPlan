package com.example.pickaplan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private EditText etUserName, etPassword;
    private TextView tvRegister;
    private Button btnLogin;
    private FirebaseAuth mAuth;

    private  FirebaseUser currentUser;


    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();

        etUserName = findViewById(R.id.userET);
        etPassword = findViewById(R.id.passET);
        btnLogin = findViewById(R.id.signInButton);
        tvRegister = findViewById(R.id.HomeText);

        tvRegister.setOnClickListener(view -> {
            startActivity(new Intent(Login.this, Register.class));
        });

        btnLogin.setOnClickListener(v -> {
            if (validateInputs()) {
                signInUser();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
         currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {



            updateUI(currentUser);
        }
    }

    private void signInUser() {
        String email = etUserName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            if (user.isEmailVerified()) {
                                Log.d(TAG, "signInWithEmail:success");
                                updateUI(user);
                            } else {
                                Toast.makeText(Login.this, "Please verify your email first", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(Login.this, "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validateInputs() {
        String email = etUserName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etUserName.setError("Email is required");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return false;
        }

        return true;
    }

    private void updateUI(FirebaseUser user) {

        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);


        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name", currentUser.getEmail());
        editor.apply();

        Log.d("gotoh","go to home");
        Intent intent = new Intent(Login.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}