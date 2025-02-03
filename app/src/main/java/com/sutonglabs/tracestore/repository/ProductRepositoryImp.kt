package com.sutonglabs.tracestore.repository

import android.content.Context
import android.util.Log
import com.sutonglabs.tracestore.api.TraceStoreAPI
import com.sutonglabs.tracestore.data.getJwtToken
import com.sutonglabs.tracestore.models.Product
import com.sutonglabs.tracestore.models.ProductResponse
import com.sutonglabs.tracestore.models.ImageUploadResponse
import com.sutonglabs.tracestore.models.ProductCreate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class ProductRepositoryImp @Inject constructor(
    private val traceStoreApiService: TraceStoreAPI
) : ProductRepository {

    override suspend fun getProduct(): ProductResponse {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("ProductRepository", "Fetching all products...")
                val response: Response<ProductResponse> = traceStoreApiService.getProducts()
                if (response.isSuccessful) {
                    Log.d("ProductRepository", "Products fetched successfully")
                    response.body() ?: ProductResponse()
                } else {
                    Log.e("ProductRepository", "Error fetching products: ${response.code()} ${response.message()}")
                    ProductResponse()
                }
            } catch (e: Exception) {
                Log.e("ProductRepository", "Error fetching products: ${e.localizedMessage}")
                throw e
            }
        }
    }

    override suspend fun getProductDetail(id: Int, context: Context): Product? {
        val token = getJwtToken(context).first()  // Assuming the token is being fetched correctly
        try {
            val response = traceStoreApiService.getProductDetail(id, "Bearer $token")
            if (response.isSuccessful) {
                val productDetailResponse = response.body()
                return productDetailResponse?.data
            } else {
                Log.e("ProductRepository", "Error fetching product detail: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error: ${e.localizedMessage}")
        }
        return null
    }

    override suspend fun addProduct(product: ProductCreate, context: Context): Product? {
        val token = getJwtToken(context).first()  // Fetch JWT token
        return try {
            val response = traceStoreApiService.addProduct("Bearer $token", product)  // Include the token in the header
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("ProductRepository", "Error adding product: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error adding product: ${e.localizedMessage}")
            null
        }
    }


    // Upload Image method
    override suspend fun uploadImage(image: MultipartBody.Part): ImageUploadResponse? {
        try {
            val response = traceStoreApiService.uploadImage(image)
            if (response.isSuccessful) {
                return response.body()
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error uploading image: ${e.localizedMessage}")
        }
        return null
    }
}

