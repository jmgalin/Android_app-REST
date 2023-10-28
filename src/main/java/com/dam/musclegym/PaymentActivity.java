package com.dam.musclegym;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

public class PaymentActivity extends AppCompatActivity {
    static String accessToken;
    private String url = "https://api.sandbox.paypal.com";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

            String input = "AXaFVHiB0dkoMfSGs816_a2RhbsgWxOw0xJ-yb46If_0cun_P1e3_5TXX6MMMWz_CnkHiwVmlz7J3biK:EHlyPOyJe9Ijc3R9GZCn8kskyRNWfX7v5dz8vqwwSpEjP3TzcfGQXOiYRr8KlqN9VdmciGNJju2ZlYBS";
            String AUTH = Base64.getEncoder().encodeToString(input.getBytes());
            AsyncHttpClient client = new AsyncHttpClient();
            client.addHeader("Accept", "application/json");
            client.addHeader("Content-type", "application/x-www-form-urlencoded");
            client.addHeader("Authorization", "Basic "+ AUTH);
            String jsonString = "grant_type=client_credentials";
            HttpEntity entity = new StringEntity(jsonString, "utf-8");

            client.post(this, "https://api-m.sandbox.paypal.com/v1/oauth2/token", entity, "application/x-www-form-urlencoded",new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                    Log.e("RESPONSE_getAccessToken", response);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String response) {
                    try {
                        JSONObject jobj = new JSONObject(response);
                        accessToken = jobj.getString("access_token");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


        Button payPalButton = findViewById(R.id.pp_btn);
        payPalButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                createOrder(); //esto pondrá en marcha el flujo del pago
            }
        });

        TextView btnvolver = (TextView) findViewById(R.id.button_volver);
        btnvolver.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentActivity.this,HomeActivity.class));
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)


    private void createOrder() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Accept", "application/json");
        client.addHeader("Content-type", "application/json");
        client.addHeader("Authorization", "Bearer " + accessToken);

        String order = "{"
                + "\"intent\": \"CAPTURE\","
                + "\"purchase_units\": [\n" +
                "      {\n" +
                "        \"amount\": {\n" +
                "          \"currency_code\": \"EUR\",\n" +
                "          \"value\": \"38.00\"\n" +
                "        }\n" +
                "      }\n" +
                "    ],\"application_context\": {\n" +
                "        \"brand_name\": \"MUSCLE_GYM\",\n" +
                "        \"return_url\": \"musclegym://checkout/success\",\n" +
                "        \"cancel_url\": \"musclegym://checkout/cancel\"\n" +
                "    }}";
        HttpEntity entity = new StringEntity(order, "utf-8");

        client.post(this, url+"/v2/checkout/orders", entity, "application/json",new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                Log.e("RESPONSE_createOrder", response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                Log.i("RESPONSE_createOrder", response);
                try {
                    JSONArray links = new JSONObject(response).getJSONArray("links");

                    //Iteración para encontrar el link adecuado
                    for (int i = 0; i < links.length(); ++i) {
                        String rel = links.getJSONObject(i).getString("rel");
                        if (rel.equals("approve")){
                            String link = links.getJSONObject(i).getString("href");//linkObj.getString("href");
                            //redirecciona a este link via CCT
                            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                            CustomTabsIntent customTabsIntent = builder.build();
                            customTabsIntent.launchUrl(PaymentActivity.this, Uri.parse(link));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    };

    public static String getMyAccessToken(){
        return accessToken;
    }
}