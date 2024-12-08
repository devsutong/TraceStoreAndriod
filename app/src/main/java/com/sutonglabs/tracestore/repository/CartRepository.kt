package com.sutonglabs.tracestore.repository

import android.content.Context
import com.sutonglabs.tracestore.models.CartResponse

interface CartRepository {
    suspend fun getCart(context: Context): CartResponse? = null
    suspend fun addToCart(productId: Int, token: String): CartResponse
}