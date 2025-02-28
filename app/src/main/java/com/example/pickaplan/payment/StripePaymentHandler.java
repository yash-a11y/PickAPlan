package com.example.pickaplan.payment;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

public class StripePaymentHandler {
    private static final String TAG = "StripePaymentHandler";
    private final AppCompatActivity activity;
    private PaymentSheet paymentSheet;

    public StripePaymentHandler(AppCompatActivity activity) {
        this.activity = activity;
        this.paymentSheet = new PaymentSheet(activity, this::onPaymentSheetResult);
    }

    public void handleServerResponse(String clientSecret) {
        Log.d(TAG, "Presenting payment sheet with client secret");

        PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("Pick A Plan")
                // Enable Google Pay
                .googlePay(new PaymentSheet.GooglePayConfiguration(
                        PaymentSheet.GooglePayConfiguration.Environment.Test,
                        "CAD" // or your currency
                ))
                // Customize appearance
                .merchantDisplayName("Pick A Plan")
                .build();

        // Present the payment sheet
        paymentSheet.presentWithPaymentIntent(
                clientSecret,
                configuration
        );
    }

    private void onPaymentSheetResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            // Payment complete
            Log.d(TAG, "Payment completed!");
            showToast("Payment successful!");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            // Payment canceled
            Log.d(TAG, "Payment canceled!");
            showToast("Payment canceled");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            // Payment failed
            Exception error = (Exception) ((PaymentSheetResult.Failed) paymentSheetResult).getError();
            Log.e(TAG, "Payment failed: " + error.getMessage());
            showToast("Payment failed: " + error.getMessage());
        }
    }

    private void showToast(String message) {
        activity.runOnUiThread(() ->
                android.widget.Toast.makeText(activity, message,
                        android.widget.Toast.LENGTH_SHORT).show()
        );
    }
}