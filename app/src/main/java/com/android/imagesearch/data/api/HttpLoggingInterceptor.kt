package com.android.imagesearch.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .build()
