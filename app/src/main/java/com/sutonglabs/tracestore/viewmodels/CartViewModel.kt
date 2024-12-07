package com.sutonglabs.tracestore.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sutonglabs.tracestore.common.CartProductState
import com.sutonglabs.tracestore.common.Resource
import com.sutonglabs.tracestore.use_case.GetCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CartViewModel  @Inject constructor(
    private val cartUseCase: GetCartUseCase
    ): ViewModel() {
        private val _state = mutableStateOf(CartProductState())
        val state: State<CartProductState> = _state
    init {
        getProduct()
    }
    private fun getProduct() {
        cartUseCase().onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _state.value = CartProductState(isLoading = true)
                }

                is Resource.Success -> {
                    _state.value = CartProductState(product = result.data?.data?.cartItems ?: emptyList())
                }

                is Resource.Error -> {
                    _state.value = CartProductState(errorMessage = result.message ?: "An unexpected error occurred")
                }
            }
        }.launchIn(viewModelScope)
    }
}