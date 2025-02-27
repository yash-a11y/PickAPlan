package com.example.pickaplan.payment;

import android.content.Context;
import android.widget.Toast;
import androidx.activity.ComponentActivity;

import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;
import com.stripe.android.paymentsheet.PaymentSheetResultCallback;

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

    private final PaymentSheet paymentSheet;
    private String customerId;
    private String ephemeralKey;
    private String clientSecret;
    private final ComponentActivity activity;

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
            showToast("Error creating payment request: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        RequestBody body = RequestBody.create(requestJson.toString(), mediaType);
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
                try {
                    JSONObject responseJson = new JSONObject(response.body().string());
                    customerId = responseJson.getString("customer");
                    ephemeralKey = responseJson.getString("ephemeralKey");
                    clientSecret = responseJson.getString("clientSecret");

                    // Log successful response
                    showToast("Payment setup successful, opening payment sheet");

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
                .setCustomer(customerId)
                .setAllowsDelayedPaymentMethods(true)
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
        ((Activity) context).runOnUiThread(() -> 
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        );
    }
}