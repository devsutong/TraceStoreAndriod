package com.sutonglabs.tracestore.api.request_models.forgot_password_mdels

data class ResetPasswordRequest(
    val resetToken: String,
    val newPassword: String
)