package com.example.hrdtool;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Base64;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.hrdtool.MainActivity.unsentFormCount;


public class OkHttp {



    private static final OkHttpClient httpClient = new OkHttpClient();


    public static void sendPostReq(final String... parameters)  {

        final String paramZero = parameters[0];
        final String paramOne = parameters[1];
        final String paramTwo = parameters[2];
        RequestBody body = new FormBody.Builder()
                .add("data", paramOne)
                .build();

        Request request = new Request.Builder()
                .url("http://10.0.2.2:3000/" + paramZero) // URL of management portal
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
                    System.out.println((responseBody));
                    if (paramZero.equals("key")){
                        MainActivity.setPublicKey(responseBody);
                        MainActivity.encryptAndSendForm(paramTwo);

                        System.out.println("started MainActivity");
                    }
                    else if (paramZero.equals("api/form")){
                        unsentFormCount -= 1;
                        if (MainActivity.reference!=null)
                        {
                            MainActivity.reference.updateFormCount();
                        }

                        //MainActivity main = new MainActivity();
                        //main.updateFormCount();
                    }
                }
            }

        });

    }
}
