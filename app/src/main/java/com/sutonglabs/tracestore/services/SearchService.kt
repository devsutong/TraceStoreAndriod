package com.sutonglabs.tracestore.services

import com.sutonglabs.tracestore.api.response_model.search_response.SearchResponse
import com.sutonglabs.tracestore.api.response_model.search_response.SuggestionResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET("search")
    suspend fun searchProducts(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): SearchResponse

    @GET("search/suggestions")
    suspend fun getSuggestions(
        @Query("q") query: String
    ): SuggestionResponse
}