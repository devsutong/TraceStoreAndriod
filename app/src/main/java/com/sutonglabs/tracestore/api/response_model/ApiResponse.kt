package com.sutonglabs.tracestore.api.response_model

data class ApiResponse(
    val status: Boolean,
    val message: String ? = null,
    val error: String ? = null
)