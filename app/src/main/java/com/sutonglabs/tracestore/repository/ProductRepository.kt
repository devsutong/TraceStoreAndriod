package com.sutonglabs.tracestore.repository

import android.content.Context
import com.sutonglabs.tracestore.models.ImageUploadResponse
import com.sutonglabs.tracestore.models.Product
import com.sutonglabs.tracestore.models.ProductCreate
import com.sutonglabs.tracestore.models.ProductResponse
import okhttp3.MultipartBody

interface ProductRepository {
    suspend fun getProduct(): ProductResponse
    suspend fun getProductDetail(id: Int, context: Context): Product?
    suspend fun addProduct(product: ProductCreate, context: Context): Product?  // Accept context for JWT token
    suspend fun uploadImage(image: MultipartBody.Part): ImageUploadResponse?
}




