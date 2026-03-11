package com.sutonglabs.tracestore.services

import com.sutonglabs.tracestore.models.Product
import retrofit2.http.GET

//todo: ALl endpoints are dumped into TraceStoreAPI.kt

interface ProductApiService {
    @GET("product/")
    suspend fun getProducts(): List<Product>
}
