package com.sutonglabs.tracestore.api.request_models.forgot_password_mdels

data class VerifyOtpRequest(
    val email: String,
    val otp: String
)