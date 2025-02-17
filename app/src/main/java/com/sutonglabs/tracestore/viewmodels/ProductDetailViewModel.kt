package com.sutonglabs.tracestore.viewmodels

import android.content.Context
import android.util.Log // Import the Log class
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.sutonglabs.tracestore.data.getJwtToken
import androidx.lifecycle.viewModelScope
import com.sutonglabs.tracestore.common.ProductDetailState
import com.sutonglabs.tracestore.common.Resource
import com.sutonglabs.tracestore.repository.CartRepository
import com.sutonglabs.tracestore.use_case.GetProductDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductDetailUseCase: GetProductDetailUseCase,
    private val cartRepository: CartRepository // Injected CartRepository
) : ViewModel() {

    private val _state = mutableStateOf(ProductDetailState())
    val state: State<ProductDetailState> = _state

    // Function to fetch product details by ID
    fun getProductDetail(id: Int, context: Context) {
        viewModelScope.launch {
            // Collect the flow returned by the use case
            getProductDetailUseCase(id, context).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = ProductDetailState(isLoading = true)
                    }
                    is Resource.Success -> {
                        _state.value = ProductDetailState(productDetail = result.data)
                    }
                    is Resource.Error -> {
                        _state.value = ProductDetailState(errorMessage = result.message ?: "An unexpected error occurred")
                    }
                }
            }
        }
    }

    // Function to add a product to the cart
    fun addToCart(productId: Int, context: Context) {
        viewModelScope.launch {
            try {
                val token = getJwtToken(context).firstOrNull()
                if (token == null) {
                    Log.e("ProductDetailViewModel", "JWT token is null")
                    return@launch
                }

                cartRepository.addToCart(productId, token)
                Log.d("ProductDetailViewModel", "Product added to cart successfully")

                // Show Toast after successfully adding the product
                Toast.makeText(context, "Item added to cart!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("ProductDetailViewModel", "Error adding product to cart: ${e.message}")
            }
        }
    }




}
