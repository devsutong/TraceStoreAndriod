package com.sutonglabs.tracestore.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sutonglabs.tracestore.common.CartProductState
import com.sutonglabs.tracestore.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _state = mutableStateOf(CartProductState())
    val state: State<CartProductState> = _state

    init {
        loadCart()
    }

    private fun loadCart() {
        Log.d("VIEW_CART", " getCart called")
        viewModelScope.launch {
            try {
                _state.value = CartProductState(isLoading = true)

                val response = cartRepository.getCart()

                _state.value = CartProductState(
                    items = response.data.items
                )

            } catch (e: Exception) {
                _state.value = CartProductState(
                    errorMessage = e.message ?: "Error loading cart"
                )
            }
        }
    }

    fun updateCartItem(cartItemId: Int, newQuantity: Int) {
        viewModelScope.launch {
            try {
                val response =
                    cartRepository.updateCartItem(cartItemId, newQuantity)

                if (response.status) {
                    val updatedItem = response.data

                    _state.value = _state.value.copy(
                        items = _state.value.items.map { item ->
                            if (item.id == cartItemId)
                                item.copy(quantity = updatedItem.quantity)
                            else item
                        }
                    )
                }

            } catch (e: Exception) {
                Log.e("CartViewModel", "Error updating cart: ${e.message}")
            }
        }
    }

    fun refreshCart() {
        loadCart()
    }
}
