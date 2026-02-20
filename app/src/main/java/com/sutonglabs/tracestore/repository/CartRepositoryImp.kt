package com.sutonglabs.tracestore.repository

import android.content.Context
import android.util.Log
import com.sutonglabs.tracestore.api.TraceStoreAPI
import com.sutonglabs.tracestore.api.UpdateCartRequest
import com.sutonglabs.tracestore.data.getJwtToken
import com.sutonglabs.tracestore.models.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CartRepositoryImp @Inject constructor(
    private val traceStoreApiService: TraceStoreAPI,
    @ApplicationContext private val context: Context
) : CartRepository {

    override suspend fun getCart(): CartResponse {
        val token = getJwtToken(context).first()
        Log.d("VIEW_CART", " getCart repository called")
        return traceStoreApiService.getCart("Bearer $token")
    }

    override suspend fun updateCartItem(
        cartItemId: Int,
        quantity: Int
    ): UpdateCartResponse {

        val token = getJwtToken(context).first()

        return traceStoreApiService.updateCartItem(
            "Bearer $token",
            UpdateCartRequest(cartItemId, quantity)
        )
    }

    override suspend fun addToCart(productId: Int): CartResponse {
        val token = getJwtToken(context).first()

        return traceStoreApiService.addToCart(
            AddToCartRequest(productId),
            "Bearer $token"
        )
    }
}
