package com.sutonglabs.tracestore.models

data class Product(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    var image: String = "",
    val price: Int = 0,
    val priceUnit: String = "inr", // Price unit like USD, INR, etc.
    val categoryIds: List<Int> = emptyList() // List of category IDs
)


data class ProductResponse(
    val status: Boolean = false,
    val data: List<Product> = emptyList()
)
data class ProductDetailResponse(
    val status: Boolean = false,
    val data: Product? = null  // Single product for ProductDetail
)

