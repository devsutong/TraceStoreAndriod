package com.sutonglabs.tracestore.repository

import android.util.Log
import com.sutonglabs.tracestore.api.request_models.forgot_password_mdels.ForgotPasswordRequest
import com.sutonglabs.tracestore.api.request_models.forgot_password_mdels.ResetPasswordRequest
import com.sutonglabs.tracestore.api.request_models.forgot_password_mdels.VerifyOtpRequest
import com.sutonglabs.tracestore.api.response_model.ApiResponse
import com.sutonglabs.tracestore.api.response_model.forgot_password_response.VerifyOtpResponse
import com.sutonglabs.tracestore.services.AuthService
import retrofit2.Response
import javax.inject.Inject

class ForgotPasswordRespository @Inject constructor(
    private val api: AuthService
) {

    suspend fun requestOtp(email: String): ApiResponse {
        return try {
            val response = api.forgotPassword(ForgotPasswordRequest(email))

            if (response.isSuccessful) {
                response.body() ?: ApiResponse(false, "Unknown error")
            } else {
                val error = response.errorBody()?.string()
                ApiResponse(
                    status = false,
                    error = error ?: "Too many requests"
                )
            }
        } catch (e: Exception) {
            Log.e("ForgotPasswordRepo", "requestOtp Exception", e)
            ApiResponse(status = false, error = "Network error: Could not reach server")
        }
    }

    suspend fun verifyOtp(email: String, otp: String): Response<VerifyOtpResponse>? {
        return try {
            api.verifyOtp(VerifyOtpRequest(email, otp))
        } catch (e: Exception) {
            Log.e("ForgotPasswordRepo", "verifyOtp Exception", e)
            null
        }
    }

    suspend fun resetPassword(token: String, password: String): Response<ApiResponse>? {
        return try {
            api.resetPassword(ResetPasswordRequest(token, password))
        } catch (e: Exception) {
            Log.e("ForgotPasswordRepo", "resetPassword Exception", e)
            null
        }
    }
}
