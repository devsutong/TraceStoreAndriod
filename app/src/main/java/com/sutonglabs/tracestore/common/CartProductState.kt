package com.sutonglabs.tracestore.common

import com.sutonglabs.tracestore.models.CartItem

data class CartProductState(
    val isLoading: Boolean = false,
    val items: List<CartItem> = emptyList(),
    val errorMessage: String = ""
)
