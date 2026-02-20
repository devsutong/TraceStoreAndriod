package com.sutonglabs.tracestore.viewmodels.state

sealed class AddProductState {
    object Idle : AddProductState()
    object Loading : AddProductState()
    object Success : AddProductState()
    data class Error(val message: String) : AddProductState()
}
