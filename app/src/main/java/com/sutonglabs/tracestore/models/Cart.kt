package com.sutonglabs.tracestore.models

data class CartResponse(
    val status: Boolean,
    val data: CartData
)

data class CartData(
    val id: Int,
    val userId: Int,
    val items: List<CartItem>,
    val createdAt: String,
    val updatedAt: String
)

data class CartItem(
    val id: Int,
    val quantity: Int,
    val product: CartProduct
)

data class CartProduct(
    val id: Int,
    val name: String,
    val price: Int,
    val image: String?
)

data class UpdateCartResponse(
    val status: Boolean,
    val message: String,
    val data: UpdatedCartItem
)

data class UpdatedCartItem(
    val id: Int,
    val quantity: Int
)
