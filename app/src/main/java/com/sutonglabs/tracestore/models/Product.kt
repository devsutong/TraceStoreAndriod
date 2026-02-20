package com.sutonglabs.tracestore.models

import com.google.gson.annotations.SerializedName

data class Product(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",

    @SerializedName("Images")
    val images: List<ProductImage> = emptyList(),

    //todo
    @SerializedName("ProductBlockchainStatus")
    val productBlockchainStatus: ProductBlockchainStatus? = null,

    val price: Double = 0.0,
    val priceUnit: String = "inr", // Price unit like USD, INR, etc.
    val categoryIds: List<Int> = emptyList(), // List of category IDs
    val blockchainStatus: Boolean = false
)


data class ProductResponse(
    val status: Boolean = false,
    val data: List<Product> = emptyList()
)
data class ProductDetailResponse(
    val status: Boolean = false,
    val data: Product? = null  // Single product for ProductDetail
)

data class ProductImage(
    val id: Int,
    val imageUrl: String,
    val position: Int
)

data class ProductBlockchainStatus(
    val productId: Int,
    val blockchainStatus: Boolean
)

