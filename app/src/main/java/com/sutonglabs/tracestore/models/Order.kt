package com.sutonglabs.tracestore.models

data class Order(
    val id: Int,
    val userID: Int,
    val totalPrice: Double,
    val status: String,
    val created_at: String,
    val items: List<OrderItem>
)

data class OrderItem(
    val productName: String,
    val image: String
)
