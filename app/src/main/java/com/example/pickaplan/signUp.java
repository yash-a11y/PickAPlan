package com.example.pickaplan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;

public class signUp extends AppCompatActivity {

//    private EditText etUserName, etPassword ;
//    private TextView etRegister;
//    private Button btnLogin;
//
//    private FirebaseAuth mAuth; // Firebase Authentication instance
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            updateUI(user);
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

//        mAuth = FirebaseAuth.getInstance();
//
//        etUserName = findViewById(R.id.usernameEditText);
//        etPassword = findViewById(R.id.passwordEditText);
//        btnLogin = findViewById(R.id.signInButton);
//        etRegister = findViewById(R.id.HomeText);
//
//        etRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(signUp.this, Register.class);
//                startActivity(intent);
//            }
//        });
//
//
//        btnLogin.setOnClickListener(v -> {
//            if (validateInputs()) {
//                signInUser();
//            }
//        });
//    }
//    private void signInUser() {
//        String email = etUserName.getText().toString().trim();
//        String password = etPassword.getText().toString().trim();
//
//
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        FirebaseUser user = mAuth.getCurrentUser();
//                        Log.d("mail",user.getEmail().toString());
//                        if (user.isEmailVerified()) {
//                            // Navigate to Home Activity
//                            Intent intent = new Intent(signUp.this, HomeActivity.class);
//                            startActivity(intent);
//                            finish();
//                        } else {
//                            Toast.makeText(signUp.this, "Please verify your email first", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(signUp.this, "Sign-In failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    private boolean validateInputs() {
//        String email = etUserName.getText().toString().trim();
//        String password = etPassword.getText().toString().trim();
//
//        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
//            Toast.makeText(signUp.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }
//
//    private void updateUI(FirebaseUser user) {
//        if (user.isEmailVerified()) {
//            startActivity(new Intent(signUp.this, HomeActivity.class));
//            finish();
//        } else {
//            user.sendEmailVerification();
//            Toast.makeText(signUp.this, "Verify email first", Toast.LENGTH_LONG).show();
//        }
//    }

    }
}