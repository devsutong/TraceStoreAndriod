package com.sutonglabs.tracestore.repository

import android.util.Log
import com.sutonglabs.tracestore.api.TraceStoreAPI
import com.sutonglabs.tracestore.api.response_model.search_response.SearchResponse
import com.sutonglabs.tracestore.api.response_model.search_response.SuggestionResponse
import com.sutonglabs.tracestore.services.SearchService
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val api: SearchService
) {
    suspend fun search(
        query: String,
        page: Int,
        limit: Int
    ): SearchResponse? {
        return try {
            api.searchProducts(query, page, limit)
        } catch (e: Exception) {
            Log.e("SearchRepository", "search Exception", e)
            null
        }
    }

    suspend fun getSuggestions(query: String): SuggestionResponse? {
        return try {
            api.getSuggestions(query)
        } catch (e: Exception) {
            null
        }
    }
}
