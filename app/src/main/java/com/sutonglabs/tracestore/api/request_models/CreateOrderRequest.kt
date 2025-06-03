package com.sutonglabs.tracestore.api.request_models

data class CreateOrderRequest(
    val products: List<Product>,
    val totalAmount: Double,
    val addressID: Int,
    val status: String
)


data class Product(
    val productID: Int,
    val quantity: Int
)

