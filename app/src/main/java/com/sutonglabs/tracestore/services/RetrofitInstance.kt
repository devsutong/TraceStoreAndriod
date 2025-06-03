package com.sutonglabs.tracestore.services

import com.sutonglabs.tracestore.api.TraceStoreAPI
import com.sutonglabs.tracestore.common.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: TraceStoreAPI by lazy {
        retrofit.create(TraceStoreAPI::class.java)
    }

}
