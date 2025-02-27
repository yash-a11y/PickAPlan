package com.example.pickaplan.payment;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

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

public class StripePaymentHandler {
    // Replace with your actual publishable key from Stripe Dashboard
    private static final String STRIPE_PUBLISHABLE_KEY = "pk_test_your_stripe_publishable_key";
    // Replace with your Spring Boot server URL
    private static final String BACKEND_URL = "http://your-server-url:8080/api/stripe";

    private PaymentSheet paymentSheet;
    private String customerId;
    private String ephemeralKey;
    private String clientSecret;
    private final Context context;

    public StripePaymentHandler(Context context) {
        this.context = context;
        PaymentConfiguration.init(context, STRIPE_PUBLISHABLE_KEY);
        paymentSheet = new PaymentSheet((Activity) context, this::onPaymentResult);
    }

    public void startPayment(String amount, String currency) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put("amount", amount);
            requestJson.put("currency", currency);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(requestJson.toString(), mediaType);
        Request request = new Request.Builder()
                .url(BACKEND_URL + "/create-payment-intent")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showToast("Payment setup failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject responseJson = new JSONObject(response.body().string());
                    customerId = responseJson.getString("customer");
                    ephemeralKey = responseJson.getString("ephemeralKey");
                    clientSecret = responseJson.getString("clientSecret");

                    ((Activity) context).runOnUiThread(() -> presentPaymentSheet());
                } catch (Exception e) {
                    showToast("Payment setup failed: " + e.getMessage());
                }
            }
        });
    }

    private void presentPaymentSheet() {
        PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("Pick A Plan")
                .setCustomer(customerId)
                .setAllowsDelayedPaymentMethods(true)
                .build();

        paymentSheet.presentWithPaymentIntent(
                clientSecret,
                configuration
        );
    }

    private void onPaymentResult(PaymentSheetResult paymentResult) {
        if (paymentResult instanceof PaymentSheetResult.Completed) {
            showToast("Payment successful!");
        } else if (paymentResult instanceof PaymentSheetResult.Canceled) {
            showToast("Payment canceled!");
        } else if (paymentResult instanceof PaymentSheetResult.Failed) {
            showToast("Payment failed: " + ((PaymentSheetResult.Failed) paymentResult).getError().getMessage());
        }
    }

    private void showToast(String message) {
        ((Activity) context).runOnUiThread(() -> 
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        );
    }
} 