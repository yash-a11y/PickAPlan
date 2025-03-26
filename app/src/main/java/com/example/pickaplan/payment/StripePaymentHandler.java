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
    private PaymentSheet.CustomerConfiguration customerConfig;

    public StripePaymentHandler(AppCompatActivity activity, String publishableKey) {
        this.activity = activity;
        PaymentConfiguration.init(activity, publishableKey);
        this.paymentSheet = new PaymentSheet(activity, this::onPaymentSheetResult);
    }

    public void handleServerResponse(String clientSecret) {
        Log.d(TAG, "Presenting payment sheet with client secret");

        PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("Pick A Plan")
                .googlePay(
                        new PaymentSheet.GooglePayConfiguration(
                        PaymentSheet.GooglePayConfiguration.Environment.Test,
                        "CAD"
                ))
                .merchantDisplayName("Pick A Plan")
                .build();

        paymentSheet.presentWithPaymentIntent(
                clientSecret,
                configuration
        );
    }

    private void onPaymentSheetResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Log.d(TAG, "Payment completed!");
            showToast("Payment successful!");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.d(TAG, "Payment canceled!");
            showToast("Payment canceled");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
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