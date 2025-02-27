package com.example.pickaplan;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {
    
    private Button payButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        
        // Initialize Razorpay Checkout
        Checkout.preload(getApplicationContext());
        
        payButton = findViewById(R.id.pay_button);
        payButton.setOnClickListener(v -> startPayment());
    }
    
    private void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("YOUR_KEY_ID"); // Replace with your actual Key ID
        
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Pick A Plan");
            options.put("description", "Plan Purchase");
            options.put("currency", "INR");
            options.put("amount", 100000); // amount in paise (e.g., 1000 = ₹10)
            options.put("prefill.email", "user@example.com");
            options.put("prefill.contact", "9999999999");
            
            checkout.open(this, options);
        } catch (Exception e) {
            Toast.makeText(this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            // Handle successful payment here
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment Failed: " + response, Toast.LENGTH_SHORT).show();
            // Handle failed payment here
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 