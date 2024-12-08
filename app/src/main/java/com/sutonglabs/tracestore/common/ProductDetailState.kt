package com.sutonglabs.tracestore.common

import com.sutonglabs.tracestore.models.Product

data class ProductDetailState(
    val isLoading: Boolean = false,
    val productDetail: Product? = null,  // Single product for Product Detail
    val errorMessage: String = ""
)
