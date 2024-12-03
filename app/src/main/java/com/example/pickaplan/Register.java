package com.example.pickaplan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private EditText etUserName, etEmail, etPassword, etConfirmPassword, etPhoneNumber;
    private Button btnRegister;
    private  TextView goToHome;

    private FirebaseAuth mAuth; // Firebase Authentication instance
    private DatabaseReference databaseReference; // Firebase Realtime Database reference
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);


        // Initialize Firebase Auth and Database Reference
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");



        etUserName = findViewById(R.id.usernameEditText);
        etEmail = findViewById(R.id.EmailEditText);
        etPassword = findViewById(R.id.passwordEditText);
        etConfirmPassword = findViewById(R.id.confirmpasswordEditText);
        etPhoneNumber = findViewById(R.id.PhonenumberEditText);
        btnRegister = findViewById(R.id.registerButton);
        goToHome = findViewById(R.id.HomeText);

//        // Navigate to HomeActivity if the user is already registered (check SharedPreferences)
//        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
//        if (sharedPreferences.getBoolean("isRegistered", false)) {
//            Intent intent = new Intent(Register.this, signUp.class);
//            startActivity(intent);
//            finish();
//        }

        goToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, signUp.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    // Save registration status in SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isRegistered", true);
                    editor.apply(); // Save changes asynchronously

                    // Show success message
                    Toast.makeText(Register.this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                    // Navigate to HomeActivity
                    Intent intent = new Intent(Register.this, HomeActivity.class);
                    startActivity(intent);
                    finish(); // Close the Register activity
                }
            }
        });

    }

    private void registerUser() {
        String username = etUserName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();

        // Create user with email and password in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Get the Firebase User ID
                        String userId = mAuth.getCurrentUser().getUid();

                        // Save user details in Firebase Realtime Database
                        User user = new User(username, email, phoneNumber);
                        databaseReference.child(userId).setValue(user)
                                .addOnCompleteListener(dbTask -> {
                                    if (dbTask.isSuccessful()) {
                                        Toast.makeText(Register.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Register.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(Register.this, "Failed to save user data: " + dbTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(Register.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validateInputs() {

        // Username regex pattern
        String usernamePattern = "^[a-zA-Z0-9_]{3,15}$";

        // Password regex pattern
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

        // Email regex pattern for specific domains
        String emailPattern = "^[a-zA-Z0-9._%+-]+@(gmail\\.com|yahoo\\.com|uwindsor\\.ca)$";

        // Phone number regex pattern for specific formats or continuous 10-digit numbers
        String phonePattern = "^(\\d{3}[- ]?\\d{3}[- ]?\\d{4}|\\d{10})$";

        // Validate username
        if (TextUtils.isEmpty(etUserName.getText().toString().trim()) ||
                !etUserName.getText().toString().trim().matches(usernamePattern)) {
            etUserName.setError("Username must be 3-15 characters and can contain letters, numbers, or underscores.");
            return false;
        }

        // Validate email
        if (TextUtils.isEmpty(etEmail.getText().toString().trim()) ||
                !etEmail.getText().toString().trim().matches(emailPattern)) {
            etEmail.setError("Email must be a valid Gmail, Yahoo, or Uwindsor address");
            return false;
        }

        // Validate password
        if (TextUtils.isEmpty(etPassword.getText().toString().trim()) ||
                !etPassword.getText().toString().trim().matches(passwordPattern)) {
            etPassword.setError("Password must be at least 8 characters long and contain uppercase,lowercase,digit, and special character.");
            return false;
        }

        // Confirm password
        if (!etPassword.getText().toString().trim().equals(etConfirmPassword.getText().toString().trim())) {
            etConfirmPassword.setError("Passwords do not match");
            return false;
        }

        // Validate phone number
        if (TextUtils.isEmpty(etPhoneNumber.getText().toString().trim()) ||
                !etPhoneNumber.getText().toString().trim().matches(phonePattern)) {
            etPhoneNumber.setError("Phone number must be in the format 365 866 4436 or 365-866-4437");
            return false;
        }

        return true;
    }
    // User class for storing user data in Firebase
    public static class User {
        public String username;
        public String email;
        public String phoneNumber;

        public User() { } // Default constructor for Firebase

        public User(String username, String email, String phoneNumber) {
            this.username = username;
            this.email = email;
            this.phoneNumber = phoneNumber;
        }
    }
}