package com.pda.hm_texas.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pda.hm_texas.impl.ApiService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetorfitHelper {

    public static final String BASE_LIVE_URL = "http://10.15.110.246:5701/";
    public static final String SERVICE_LIVE_URL = "http://192.168.0.101:5701/";
    public static final String BASE_TEST_URL = "http://1.215.46.190:5701/";
    public static final String BASE_LOCAL_URL = "http://192.168.10.121:5701/";

    //public static final String USE_URL = "http://1.215.46.190:5701/";
    //public static final String USE_URL = "http://192.168.219.128:5702/";
    //public static final String USE_URL =  "http://192.168.0.36:8080/";
    public static final String USE_URL =  "http://192.168.0.36:9090/";
    //public static final String USE_URL =  "http://192.168.50.99:8080/";
    //public static final String USE_URL =  "http://4.255.209.126:8080/";


    public static ApiService getApiService(String url){

        return getInstance(url).create(ApiService.class);
    }


    private static Retrofit getInstance(String url)
    {
        Gson gson = new GsonBuilder().setLenient().create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS) // 연결 타임아웃 10초
                .readTimeout(60, TimeUnit.SECONDS)   // 읽기 타임아웃 30초
                .writeTimeout(60, TimeUnit.SECONDS) // 쓰기 타임아웃 15초
                .build();

        return new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

}
