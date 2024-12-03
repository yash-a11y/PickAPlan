package com.example.pickaplan;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private EditText etUserName, etEmail, etPassword, etConfirmPassword, etPhoneNumber;
    private Button btnRegister;
    private TextView goToHome, goToSignIn;

    private FirebaseAuth mAuth; // Firebase Authentication instance
    private DatabaseReference databaseReference; // Firebase Realtime Database reference

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            updateUI(user);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth and Database Reference
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize Views
        etUserName = findViewById(R.id.usernameEditText);
        etEmail = findViewById(R.id.EmailEditText);
        etPassword = findViewById(R.id.passwordEditText);
        etConfirmPassword = findViewById(R.id.confirmpasswordEditText);
        etPhoneNumber = findViewById(R.id.PhonenumberEditText);
        btnRegister = findViewById(R.id.registerButton);
        goToHome = findViewById(R.id.HomeText);
        //goToSignIn = findViewById(R.id.goToSignIn);

        // If the user is already logged in, navigate to the HomeActivity
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // If user is already authenticated, navigate to HomeActivity
            Intent intent = new Intent(Register.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        // On "Go to Sign In" link click, navigate to SignIn activity
        goToHome.setOnClickListener(view -> {
            Intent intent = new Intent(Register.this, signUp.class);
            startActivity(intent);
        });

        // On Register button click, create a new user
        btnRegister.setOnClickListener(v -> {
            if (validateInputs()) {
                registerUser(); // Call Firebase Register method
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
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userId = user.getUid();

                        // Save user details in Firebase Realtime Database
                        User userDetails = new User(username, email, phoneNumber);
                        databaseReference.child(userId).setValue(userDetails)
                                .addOnCompleteListener(dbTask -> {
                                    if (dbTask.isSuccessful()) {
                                        // Send email verification
                                        user.sendEmailVerification()
                                                .addOnCompleteListener(verificationTask -> {
                                                    if (verificationTask.isSuccessful()) {
                                                        Toast.makeText(Register.this, "Registration Successful! Verification email sent.", Toast.LENGTH_LONG).show();
                                                        Intent intent = new Intent(Register.this, signUp.class); // Go to Sign In after registration
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(Register.this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
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
            etPassword.setError("Password must be at least 8 characters long and contain uppercase, lowercase, digits, and special characters.");
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

    private void updateUI(FirebaseUser user) {
        if (user.isEmailVerified()) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_LONG).show();
                        }
                    });
            startActivity(new Intent(this, signUp.class));
        }
    }
}
