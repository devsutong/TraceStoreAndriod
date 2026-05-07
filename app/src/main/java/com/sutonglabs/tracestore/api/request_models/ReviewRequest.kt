package com.sutonglabs.tracestore.api.request_models

data class ReviewRequest(
    val productId: Int,
    val rating: Int,
    val reviewText: String
)
