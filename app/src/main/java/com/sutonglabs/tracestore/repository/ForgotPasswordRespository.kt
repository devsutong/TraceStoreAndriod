package com.sutonglabs.tracestore.repository

import com.sutonglabs.tracestore.api.request_models.forgot_password_mdels.ForgotPasswordRequest
import com.sutonglabs.tracestore.api.request_models.forgot_password_mdels.ResetPasswordRequest
import com.sutonglabs.tracestore.api.request_models.forgot_password_mdels.VerifyOtpRequest
import com.sutonglabs.tracestore.api.response_model.ApiResponse
import com.sutonglabs.tracestore.services.AuthService
import javax.inject.Inject

class ForgotPasswordRespository @Inject constructor(
    private val api: AuthService
) {

    suspend fun requestOtp(email: String): ApiResponse {

        val response = api.forgotPassword(ForgotPasswordRequest(email))

        return if (response.isSuccessful) {

            response.body() ?: ApiResponse(false, "Unknown error")

        } else {

            val error = response.errorBody()?.string()

            ApiResponse(
                status = false,
                error = error ?: "Too many requests"
            )

        }

    }
    suspend fun verifyOtp(email: String, otp: String) =
        api.verifyOtp(VerifyOtpRequest(email, otp))

    suspend fun resetPassword(token: String, password: String) =
        api.resetPassword(ResetPasswordRequest(token, password))
}