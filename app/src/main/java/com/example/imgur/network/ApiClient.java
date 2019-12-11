package com.example.imgur.network;

import com.example.imgur.BuildConfig;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

  private static Retrofit retrofit = null;

  public static Retrofit getApiClient() {
    if (retrofit == null) {
      retrofit = new Retrofit.Builder().baseUrl(BuildConfig.apiUrl)
          .addConverterFactory(GsonConverterFactory.create())
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .build();
    }
    return retrofit;
  }
}
