package com.sutonglabs.tracestore.models

data class AddToCartRequest(
    val userId: Int,
    val productId: Int,
    val quantity: Int = 1 // Default quantity
)
