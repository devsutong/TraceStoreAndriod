package com.sutonglabs.tracestore.models

data class Product(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val image: String = "",
    val price: Int = 0,
    val priceUnit: String = "",
    val createdAt: String = "",
    val updatedAt: String = ""
)

data class ProductResponse(
    val status: Boolean = false,
    val data: List<Product> = emptyList()
)
data class ProductDetailResponse(
    val status: Boolean = false,
    val data: Product? = null  // Single product for ProductDetail
)

