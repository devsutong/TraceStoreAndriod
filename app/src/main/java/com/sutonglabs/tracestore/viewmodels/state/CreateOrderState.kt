package com.sutonglabs.tracestore.viewmodels.state

import com.sutonglabs.tracestore.api.response_model.CreateOrderResponse

data class CreateOrderState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false, // New field to track success state
    val address: CreateOrderResponse? = null,
    val errorMessage: String = ""
)
