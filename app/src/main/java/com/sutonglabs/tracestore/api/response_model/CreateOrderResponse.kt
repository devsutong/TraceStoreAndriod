package com.sutonglabs.tracestore.api.response_model

data class CreateOrderResponse(
    val products: List<Product>,
    val totalAmount: String,
    val addressID: Int
)

data class OrdersResponse(
    val status: Boolean,
    val data: List<Order>
)

data class Order(
    val id: Int,
    val userID: Int,
    val totalAmount: Int,
    val addressID: Int,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val OrderItems: List<OrderItem>
)

data class OrderItem(
    val quantity: Int,
    val Product: Product
)

data class Product(
    val id: Int,
    val name: String,
    val image: String,
    val price: Int
)

