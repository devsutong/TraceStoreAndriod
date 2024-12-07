package com.sutonglabs.tracestore.repository

import android.content.Context
import com.sutonglabs.tracestore.api.TraceStoreAPI
import com.sutonglabs.tracestore.data.getJwtToken
import com.sutonglabs.tracestore.data.decodeJwt
import com.sutonglabs.tracestore.models.AddToCartRequest
import com.sutonglabs.tracestore.models.CartResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.await
import javax.inject.Inject

class CartRepositoryImp @Inject constructor(
    private val traceStoreApiService: TraceStoreAPI,
): CartRepository {

    override suspend fun getCart(context: Context): CartResponse? {
        val token = getJwtToken(context).first()
        return withContext(Dispatchers.IO) {
            traceStoreApiService.getCart("Bearer $token").await()
        }
    }

    override suspend fun addToCart(productId: Int, token: String): CartResponse {
        val userId = getUserIdFromToken(token) // Extract userId from token
        val request = AddToCartRequest(userId = userId, productId = productId)

        return withContext(Dispatchers.IO) {
            val response = traceStoreApiService.addToCart(request, "Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                throw Exception("Failed to add product to cart. ${response.errorBody()?.string()}")
            }
        }
    }

    // Function to extract userId from JWT token
    private fun getUserIdFromToken(token: String): Int {
        val decodedToken = decodeJwt(token)
        return decodedToken.getInt("userId") // Assuming your token has a userId field
    }
}

