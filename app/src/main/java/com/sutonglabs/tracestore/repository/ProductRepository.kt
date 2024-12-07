package com.sutonglabs.tracestore.repository

import android.content.Context
import com.sutonglabs.tracestore.models.Product
import com.sutonglabs.tracestore.models.ProductResponse

interface ProductRepository {
    suspend fun getProduct(): ProductResponse
    suspend fun getProductDetail(id: Int, context: Context): Product?

}
