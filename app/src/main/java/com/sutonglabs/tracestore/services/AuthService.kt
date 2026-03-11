package com.sutonglabs.tracestore.services

import com.sutonglabs.tracestore.api.request_models.forgot_password_mdels.ForgotPasswordRequest
import com.sutonglabs.tracestore.api.request_models.forgot_password_mdels.ResetPasswordRequest
import com.sutonglabs.tracestore.api.request_models.forgot_password_mdels.VerifyOtpRequest
import com.sutonglabs.tracestore.api.response_model.ApiResponse
import com.sutonglabs.tracestore.api.response_model.VerifyOtpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("forgot-password")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequest
    ): Response<ApiResponse>

    @POST("verify-reset-otp")
    suspend fun verifyOtp(
        @Body request: VerifyOtpRequest
    ): Response<VerifyOtpResponse>

    @POST("reset-password")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): Response<ApiResponse>
}