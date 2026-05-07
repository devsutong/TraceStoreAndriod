package com.sutonglabs.tracestore.common

import com.sutonglabs.tracestore.models.Product
import com.sutonglabs.tracestore.models.Review

data class ProductDetailState(
    val isLoading: Boolean = false,
    val productDetail: Product? = null,  // Single product for Product Detail
    val errorMessage: String = "",
    val canReview: Boolean = false,
    val reviews: List<Review> = emptyList()
)
