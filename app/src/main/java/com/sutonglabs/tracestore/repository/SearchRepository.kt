package com.sutonglabs.tracestore.repository

import com.sutonglabs.tracestore.api.TraceStoreAPI
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val traceStoreApiService: TraceStoreAPI
) {
    suspend fun search(
        query: String,
        page: Int,
        limit: Int
    ) = traceStoreApiService.searchProducts(query, page, limit)
}
