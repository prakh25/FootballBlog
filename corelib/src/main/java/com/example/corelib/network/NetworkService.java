package com.example.corelib.network;

import com.example.corelib.BuildConfig;
import com.example.corelib.MyBlogApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by prakh on 16-11-2017.
 */

public class NetworkService {

    private static final String TAG = "NetworkService";

    private static final int HTTP_CACHE_SIZE = 10*1024*1024; //10 MB
    private static final String CACHE_DIR = "http_cache";

    private static final String BASE_URL = "http://192.168.0.23/myFootballBlog/";

    private static final int HTTP_READ_TIMEOUT = 10000;
    private static final int HTTP_CONNECT_TIMEOUT = 6000;


    private static Gson getGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    private static Retrofit provideRetrofit(String baseUrl) {

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(provideOkhttpClient())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .build();
    }

    private static OkHttpClient provideOkhttpClient() {

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient().newBuilder();
        httpClientBuilder.connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(HTTP_READ_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.addInterceptor(makeLoggingInterceptor());
        return httpClientBuilder.build();
    }

    private static Cache provideCache() {
        Cache cache;
        try {
            cache = new Cache(new File(MyBlogApplication.getCache(), CACHE_DIR),
                    HTTP_CACHE_SIZE);
        } catch (Exception e) {
            cache = null;
            e.printStackTrace();
        }

        return cache;
    }

    private static HttpLoggingInterceptor makeLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.NONE);
        return logging;
    }

    public static MyBlogApi provideBlogPost() {
        return provideRetrofit(BASE_URL).create(MyBlogApi.class);
    }
}
