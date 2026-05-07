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
    val totalAmount: String,
    val addressID: Int,
    val orderNumber: String,
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
    val price: Int,
    val uuid: String? = null,
    val Images: List<ProductImage>? = null
)

data class ProductImage(
    val id: Int,
    val uuid: String,
    val position: Int,
    val extension: String,
    val imageUrl: String
)
