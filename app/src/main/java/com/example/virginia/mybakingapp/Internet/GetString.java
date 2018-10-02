package com.example.virginia.mybakingapp.Internet;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetString {
    OkHttpClient client = new OkHttpClient();
    String myResponse;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            myResponse=response.body().string();
            return myResponse;
        }
    }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)

         public static void main(String[] args) throws IOException {
            GetString example = new GetString();
            String response = example.run("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json");
            System.out.println(response);

    }
    public String getStringBack(){
        return myResponse;
    }

}
