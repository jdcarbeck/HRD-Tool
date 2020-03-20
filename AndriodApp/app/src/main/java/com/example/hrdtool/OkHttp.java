package com.example.hrdtool;

import android.os.AsyncTask;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttp extends AsyncTask<String, Void, String> {

    private static final OkHttpClient httpClient = new OkHttpClient();


    @Override
    protected String doInBackground(String... parameters) {
        

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                parameters[0]
        );

        Request request = new Request.Builder()
                .url("https://webhook.site/64eb19f1-ade7-4c8e-aa27-5b4e633cc901")
                .addHeader("User-Agent", "OkHttp Bot")
                .post(body)
                .build();

        try {
            // Get response
            Response response = httpClient.newCall(request).execute();
            System.out.println(response.body().string());
        }
        catch (java.io.IOException e){
            e.printStackTrace();
        }
        return "";

    }

}
