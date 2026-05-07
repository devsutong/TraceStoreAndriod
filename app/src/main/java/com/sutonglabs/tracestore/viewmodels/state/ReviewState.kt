package com.sutonglabs.tracestore.viewmodels.state

data class ReviewState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
