package com.example.grobi.whazzupnearevenly;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by grobi on 01.05.17.
 */

public class ApiFactory {

    private Retrofit retrofit;
    private static ApiFactory apiFactory;

    private ApiFactory(){
        Gson gson = new GsonBuilder().setDateFormat("yyy-MM-dd'T'HH:mm:ssZ").create();

        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(20, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.foursquare.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(builder.build())
                .build();
    }

    public static ApiFactory getApiFactory(){
        if (ApiFactory.apiFactory == null)
            ApiFactory.apiFactory = new ApiFactory();

        return ApiFactory.apiFactory;
    }

    public Retrofit getRetrofit(){
        return retrofit;
    }
}
