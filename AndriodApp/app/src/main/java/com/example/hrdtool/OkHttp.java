package com.example.hrdtool;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

        final String paramOne = parameters[1];
        RequestBody body = RequestBody.create(
                MediaType.parse("charset=utf-8"),
                parameters[1] + parameters[2]
        );

        Request request = new Request.Builder()
                .url("http://192.168.0.11:3000/HRform-router/" + parameters[0])
                .addHeader("User-Agent", "OkHttp Bot")
                .post(body)
                .build();

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
                    if (paramOne.equals("")){
                        MainActivity.setPublicKey(responseBody);
                        System.out.println((responseBody));
                    }

                }
            }

        });

    }
}
