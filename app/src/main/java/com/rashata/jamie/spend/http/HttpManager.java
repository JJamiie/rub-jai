package com.rashata.jamie.spend.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jjamierashata on 5/7/16 AD.
 */
public class HttpManager {
    private static HttpManager instance;
    private ApiService apiService;

    private HttpManager() {
        //Set timeout
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getBaseURL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public static HttpManager getInstance() {
        if (instance == null)
            instance = new HttpManager();
        return instance;
    }

    public static String getBaseURL() {
        return "http://api.fixer.io/";
    }

    public ApiService getApiService() {
        return apiService;
    }


}

