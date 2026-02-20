package com.sutonglabs.tracestore.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.sutonglabs.tracestore.api.TraceStoreAPI
import com.sutonglabs.tracestore.data.auth.TokenProvider
import com.sutonglabs.tracestore.models.*
import com.sutonglabs.tracestore.viewmodels.helper.ImageFileHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import javax.inject.Inject

class ProductRepositoryImp @Inject constructor(
    private val traceStoreApiService: TraceStoreAPI,
    private val tokenProvider: TokenProvider
) : ProductRepository {

    override suspend fun getProduct(): ProductResponse =
        withContext(Dispatchers.IO) {
            val response = traceStoreApiService.getProducts()

            if (!response.isSuccessful || response.body() == null) {
                Log.e(
                    "ProductRepository",
                    "Fetch products failed: ${response.code()} ${response.message()}"
                )
                throw Exception("Failed to fetch products")
            }

            response.body()!!
        }

    override suspend fun getProductDetail(id: Int): Product =
        withContext(Dispatchers.IO) {
            val token = tokenProvider.getToken()
            val response =
                traceStoreApiService.getProductDetail(id, "Bearer $token")

            if (!response.isSuccessful || response.body()?.data == null) {
                Log.e(
                    "ProductRepository",
                    "Fetch product detail failed: ${response.code()}"
                )
                throw Exception("Failed to fetch product detail")
            }

            response.body()!!.data!!
        }

    override suspend fun addProduct(product: ProductCreate): ProductDetailResponse =
        withContext(Dispatchers.IO) {

            val token = tokenProvider.getToken()
            val response =
                traceStoreApiService.addProduct("Bearer $token", product)

            val body = response.body()

            if (!response.isSuccessful || body == null) {
                Log.e(
                    "ProductRepository",
                    "Add product failed: ${response.code()} ${response.message()}"
                )
                throw Exception("Product creation failed")
            }

            Log.d("ProductRepository", "Product creation successful")
            body
        }


    override suspend fun uploadImages(
        context: Context,
        imageUris: List<Uri>
    ): ImageUploadResponse {

        val token = tokenProvider.getToken()

        val parts = imageUris.map { uri ->
            val file = ImageFileHelper.uriToFile(context, uri)
            val body = file
                .asRequestBody("image/*".toMediaTypeOrNull())

            MultipartBody.Part.createFormData(
                name = "image",
                filename = file.name,
                body = body
            )
        }

        val response =
            traceStoreApiService.uploadProductImages(
                "Bearer $token",
                parts
            )

        if (!response.isSuccessful || response.body() == null) {
            throw Exception("Image upload failed")
        }

        return response.body()!!
    }

    override suspend fun syncProductToBlockchain(productId: Int): Product =
        withContext(Dispatchers.IO) {
            val token = tokenProvider.getToken()
            val response =
                traceStoreApiService.syncProductToBlockchain(
                    "Bearer $token",
                    productId
                )

            if (!response.isSuccessful || response.body()?.data == null) {
                Log.e(
                    "ProductRepository",
                    "Blockchain sync failed: ${response.code()}"
                )
                throw Exception("Blockchain sync failed")
            }

            response.body()!!.data!!
        }
}
