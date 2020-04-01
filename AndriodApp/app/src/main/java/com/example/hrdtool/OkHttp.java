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
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class OkHttp {

    public static final String URL = "http://10.0.2.2:3000/";
    private static final OkHttpClient httpClient = new OkHttpClient();
    public static String publicKeyReceived;


    public static void sendPostReq(String... parameters)  {

//        System.out.println("Body should contain: " + parameters[1] + parameters[2]);
        final String paramOne = parameters[1];
//        RequestBody body = RequestBody.create(
//                MediaType.parse("charset=utf-8"),
//                parameters[1] + parameters[2]
//        );
        RequestBody body = new FormBody.Builder()
                .add("data", parameters[1])
                .build();

        Request request = new Request.Builder()
                .url(URL + parameters[0])
                .addHeader("User-Agent", "OkHttp Bot")
                .post(body)
                .build();

        System.out.println(request);

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
                    System.out.println((responseBody));
                    if (paramZero.equals("getPublicKey")){
                        MainActivity.setPublicKey(responseBody);
                        MainActivity.sendForm(paramTwo);
                        System.out.println("started MainActivity");
                    }
                }
            }

        });
    }
}
