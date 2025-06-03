package com.sutonglabs.tracestore.models

data class SellerOrderResponse(
    val orderID: Int,
    val buyer: Buyer,
    val items: List<SellerOrderItem>
)

data class Buyer(
    val id: Int,
    val username: String,
    val email: String
)

data class SellerOrderItem(
    val product: Product,
    val quantity: Int
)
data class SellerOrdersResponseWrapper(
    val status: String,
    val data: List<SellerOrderResponse>
)
