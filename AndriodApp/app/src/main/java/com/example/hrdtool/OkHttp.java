package com.example.hrdtool;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.security.PublicKey;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttp {

    private static final OkHttpClient httpClient = new OkHttpClient();
    public static String publicKeyReceived;


    public static void sendPostReq(String... parameters)  {


        RequestBody body = RequestBody.create(
                MediaType.parse("charset=utf-8"),
                parameters[1] + parameters[2]
        );

        Request request = new Request.Builder()
                .url("https://webhook.site/d4e2ccf6-0b30-45bc-b567-a6bd82047ff6")
                .addHeader("User-Agent", "OkHttp Bot")
                .post(body)
                .build();


        // Get response
        //Response response =
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                   String responseBody = response.body().string();
                   MainActivity.setPublicKey(responseBody);
                   System.out.println((responseBody));
                }
            }
            //publicKeyReceived = response.body().string();
        });

    }
}
