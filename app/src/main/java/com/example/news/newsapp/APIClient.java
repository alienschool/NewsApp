package com.example.news.newsapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class APIClient {
    private  static final String BaseUri="http://dibukhanmathematician.com/news/php/";
    private static Retrofit retrofit;

    public static Retrofit getApiClient()
    {
        if(retrofit==null)
        {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit= new Retrofit.Builder().baseUrl(BaseUri).
                    addConverterFactory(GsonConverterFactory.create(gson)).build();

        }
        return retrofit;
    }
}
