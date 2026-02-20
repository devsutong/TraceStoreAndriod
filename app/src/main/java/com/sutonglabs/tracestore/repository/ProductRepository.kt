package com.sutonglabs.tracestore.repository

import android.content.Context
import android.net.Uri
import com.sutonglabs.tracestore.models.ImageUploadResponse
import com.sutonglabs.tracestore.models.Product
import com.sutonglabs.tracestore.models.ProductCreate
import com.sutonglabs.tracestore.models.ProductDetailResponse
import com.sutonglabs.tracestore.models.ProductResponse
import okhttp3.MultipartBody

interface ProductRepository {
    suspend fun getProduct(): ProductResponse
    suspend fun getProductDetail(id: Int): Product?
    suspend fun addProduct(product: ProductCreate): ProductDetailResponse
    suspend fun uploadImages(context: Context, imageUris: List<Uri>): ImageUploadResponse
    suspend fun syncProductToBlockchain(productId: Int): Product?
}




