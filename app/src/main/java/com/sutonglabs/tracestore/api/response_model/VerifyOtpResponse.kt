package com.sutonglabs.tracestore.api.response_model

data class VerifyOtpResponse(
    val status: Boolean,
    val message: String,
    val resetToken: String
)