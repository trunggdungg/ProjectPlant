package com.example.projectplant.retrofit;


import com.example.projectplant.utils.UnsafeOkHttpClient;
import com.example.projectplant.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;


import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        OkHttpClient unsafeOkHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();// dung de bo qua kiem tra SSL khi ket noi voi server
        Gson gson = new GsonBuilder()
                .setLenient() // Cho phép JSON không đúng chuẩn
                .create();
        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(Utils.BASE_URL) // Set the base URL here
                    .client(unsafeOkHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
