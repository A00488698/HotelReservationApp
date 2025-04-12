package com.example.hotelreservationapp.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitClient {
    private const val BASE_URL = "https://Sarahzyx.pythonanywhere.com/"
//    private const val BASE_URL = "http://10.0.2.2:8000/"  // 本地测试地址，使用 Android 模拟器需用 10.0.2.2 映射到 localhost
    // 创建日志拦截器
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }


    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}