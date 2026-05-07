package com.sutonglabs.tracestore.api.response_model.forgot_password_response

data class VerifyOtpResponse(
    val status: Boolean,
    val message: String,
    val resetToken: String
)