package com.example.pickaplan.payment;

import android.content.Context;
import android.widget.Toast;
import androidx.activity.ComponentActivity;

import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StripePaymentHandler implements PaymentSheet.PaymentSheetResultCallback {
    private static final String STRIPE_PUBLISHABLE_KEY = "";
    private static final String BACKEND_URL = "http://10.0.2.2:8080/api/stripe";

    private final PaymentSheet paymentSheet;
    private String customerId;
    private String ephemeralKey;
    private String clientSecret;
    private final ComponentActivity activity;

    public StripePaymentHandler(ComponentActivity activity) {
        this.activity = activity;
        PaymentConfiguration.init(activity, STRIPE_PUBLISHABLE_KEY);
        paymentSheet = new PaymentSheet(activity, this);
    }

    public void startPayment(String amount, String currency) {
        OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build();

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put("amount", amount);
            requestJson.put("currency", currency);
            requestJson.put("description", "Payment for Pick A Plan");
        } catch (Exception e) {
            showToast("Error creating payment request: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        showToast("Sending payment request to: " + BACKEND_URL);

        RequestBody body = RequestBody.create(mediaType, requestJson.toString());
        Request request = new Request.Builder()
                .url(BACKEND_URL + "/create-payment-intent")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String errorMessage = "Network error: " + e.getMessage();
                showToast(errorMessage);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    showToast("Server error: " + response.code() + " - " + response.message());
                    return;
                }

                try {
                    String responseBody = response.body().string();
                    JSONObject responseJson = new JSONObject(responseBody);
                    
                    if (!responseJson.has("customer") || !responseJson.has("ephemeralKey") || !responseJson.has("clientSecret")) {
                        showToast("Invalid server response: Missing required fields");
                        return;
                    }

                    customerId = responseJson.getString("customer");
                    ephemeralKey = responseJson.getString("ephemeralKey");
                    clientSecret = responseJson.getString("clientSecret");

                    showToast("Payment setup successful, opening payment sheet");
                    
                    activity.runOnUiThread(() -> presentPaymentSheet());
                } catch (Exception e) {
                    String errorMessage = "Error processing server response: " + e.getMessage();
                    showToast(errorMessage);
                    e.printStackTrace();
                }
            }
        });
    }

    private void presentPaymentSheet() {
        PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("Pick A Plan")
                .setDefaultBillingDetails(
                    PaymentSheet.BillingDetails.builder()
                        .build()
                )
                .setBillingDetailsCollectionConfiguration(
                    new PaymentSheet.BillingDetailsCollectionConfiguration.Builder()
                        .setCollectEmail(true)
                        .build()
                )
                .setAllowsDelayedPaymentMethods(true)
                .setGooglePay(new PaymentSheet.GooglePayConfiguration(
                    PaymentSheet.GooglePayConfiguration.Environment.Test,
                    "CAD"
                ))
                .build();

        paymentSheet.presentWithPaymentIntent(clientSecret, configuration);
    }

    @Override
    public void onPaymentSheetResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            showToast("Payment successful!");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            showToast("Payment canceled!");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            showToast("Payment failed: " + ((PaymentSheetResult.Failed) paymentSheetResult).getError().getMessage());
        }
    }

    private void showToast(String message) {
        activity.runOnUiThread(() -> 
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        );
    }
} 