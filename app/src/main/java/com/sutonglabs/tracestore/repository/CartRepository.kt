package com.sutonglabs.tracestore.repository

import com.sutonglabs.tracestore.models.CartResponse
import android.content.Context
import com.sutonglabs.tracestore.models.UpdateCartResponse

interface CartRepository {
    suspend fun getCart(): CartResponse
    suspend fun addToCart(productId: Int): CartResponse
    suspend fun updateCartItem(cartItemId: Int, quantity: Int): UpdateCartResponse

    //yet to implenent
    //suspend fun removeFromCart(productId: Int, token: String): CartResponse
    //suspend fun addQuantity(productId: Int, token: String): CartResponse
    //suspend fun removeQuantity(productId: Int, token: String): CartResponse

}