package com.sutonglabs.tracestore.api.request_models

data class UpdateUserRequest(
    val firstName: String,
    val lastName: String,
    val age: Int
)
