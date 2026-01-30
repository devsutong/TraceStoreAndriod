package com.sutonglabs.tracestore.api.response_model

import com.sutonglabs.tracestore.models.Product

data class SearchResponse(
    val results: List<Product>,
    val total: Int,
    val page: Int,
    val limit: Int
)
