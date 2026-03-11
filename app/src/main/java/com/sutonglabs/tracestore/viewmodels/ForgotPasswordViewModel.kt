package com.sutonglabs.tracestore.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sutonglabs.tracestore.api.response_model.*
import com.sutonglabs.tracestore.repository.ForgotPasswordRespository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val repository: ForgotPasswordRespository
) : ViewModel() {

    private val _otpState = MutableStateFlow<ApiResponse?>(null)
    val otpState: StateFlow<ApiResponse?> = _otpState

    private val _verifyOtpState = MutableStateFlow<VerifyOtpResponse?>(null)
    val verifyOtpState: StateFlow<VerifyOtpResponse?> = _verifyOtpState

    private val _resetState = MutableStateFlow<ApiResponse?>(null)
    val resetState: StateFlow<ApiResponse?> = _resetState

    fun requestOtp(email: String) {

        viewModelScope.launch {

            val result = repository.requestOtp(email)

            _otpState.value = result

        }
    }

    fun verifyOtp(email: String, otp: String) {

        viewModelScope.launch {

            val response = repository.verifyOtp(email, otp)

            if (response.isSuccessful) {
                _verifyOtpState.value = response.body()
            }

        }

    }

    fun resetPassword(token: String, password: String) {

        viewModelScope.launch {

            val response = repository.resetPassword(token, password)

            if (response.isSuccessful) {
                _resetState.value = response.body()
            }

        }

    }
}