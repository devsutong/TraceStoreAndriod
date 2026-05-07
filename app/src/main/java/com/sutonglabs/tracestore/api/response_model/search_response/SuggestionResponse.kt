package com.sutonglabs.tracestore.api.response_model.search_response

data class SuggestionResponse(
    val categories: List<String>,
    val products: List<String>
)