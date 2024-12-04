package com.example.pickaplan;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private EditText etUserName, etEmail, etPassword, etConfirmPassword, etPhoneNumber;
    private Button btnRegister;
    private TextView goToHome;

    private FirebaseAuth mAuth; // Firebase Authentication instance
    private DatabaseReference databaseReference; // Firebase Realtime Database reference

    @Override
    protected void onStart() {
        super.onStart();
        // Check if the user is already logged in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            updateUI(user); // If already logged in, go to HomeActivity
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
        etUserName = findViewById(R.id.userET);
        etEmail = findViewById(R.id.EmailEditText);
        etPassword = findViewById(R.id.passET);
        etConfirmPassword = findViewById(R.id.confirmpasswordEditText);
        etPhoneNumber = findViewById(R.id.PhonenumberEditText);
        btnRegister = findViewById(R.id.registerButton);
        goToHome = findViewById(R.id.HomeText);

        // On "Go to Sign In" link click, navigate to SignIn activity
        goToHome.setOnClickListener(view -> {
            Intent intent = new Intent(Register.this, Login.class);
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

        // Check if Firebase is initialized properly
        if (mAuth == null || databaseReference == null) {
            Log.e(TAG, "Firebase initialization failed.");
            Toast.makeText(this, "Something went wrong. Please try again later.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create user with email and password in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Get the Firebase User ID
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();

                            // Save user details in Firebase Realtime Database
                            User userDetails = new User(username, email, phoneNumber);
                            databaseReference.child(userId).setValue(userDetails)
                                    .addOnCompleteListener(dbTask -> {
                                        if (dbTask.isSuccessful()) {
                                            // Send email verification
                                            sendEmailVerification(user);
                                        } else {
                                            // Show error if data couldn't be saved
                                            Log.e(TAG, "Failed to save user data: " + dbTask.getException().getMessage());
                                            Toast.makeText(Register.this, "Failed to save user data.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        // Handle user already exists or other errors
                        handleRegistrationError(task);
                    }
                });
    }

    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Register.this, "Registration Successful! Verification email sent.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Register.this, Login.class); // Go to Sign In after registration
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Register.this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handleRegistrationError(@NonNull Task<AuthResult> task) {
        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
            Toast.makeText(Register.this, "Email is already in use. Please log in.", Toast.LENGTH_SHORT).show();
        } else {
            Log.e(TAG, "Registration failed: " + task.getException().getMessage());
            Toast.makeText(Register.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
        }
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
            // If user is verified, go to HomeActivity
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else {
            // If user is not verified, send verification email
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_LONG).show();
                        }
                    });
            startActivity(new Intent(this, Login.class));
        }
    }
}
