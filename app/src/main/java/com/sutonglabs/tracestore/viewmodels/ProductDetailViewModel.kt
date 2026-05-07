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

import com.sutonglabs.tracestore.repository.ProductRepository
import com.sutonglabs.tracestore.api.request_models.ReviewRequest
import com.sutonglabs.tracestore.repository.ReviewRepository

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductDetailUseCase: GetProductDetailUseCase,
    private val cartRepository: CartRepository, // Injected CartRepository
    private val productRepository: ProductRepository, //Injected productRepository
    private val reviewRepository: ReviewRepository // Injected reviewRepository
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
                        // Use .copy() to preserve existing data during refresh
                        _state.value = _state.value.copy(isLoading = true)
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            productDetail = result.data,
                            isLoading = false,
                            errorMessage = "" // Clear any previous error
                        )
                        checkCanReview(id, context)
                        getReviews(id)
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            errorMessage = result.message ?: "An unexpected error occurred",
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    private fun checkCanReview(productId: Int, context: Context) {
        viewModelScope.launch {
            try {
                val token = getJwtToken(context).firstOrNull()
                if (token != null) {
                    val response = reviewRepository.canReview(token, productId)
                    _state.value = _state.value.copy(canReview = response.canReview)
                }
            } catch (e: Exception) {
                Log.e("ProductDetailViewModel", "Error checking canReview: ${e.message}")
            }
        }
    }

    private fun getReviews(productId: Int) {
        viewModelScope.launch {
            try {
                val response = reviewRepository.getReviews(productId)
                // Postman shows count and rows. rows contains the list of reviews.
                _state.value = _state.value.copy(reviews = response.rows)
            } catch (e: Exception) {
                Log.e("ProductDetailViewModel", "Error fetching reviews: ${e.message}")
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
                    Toast.makeText(context, "Please login to add items to cart", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                cartRepository.addToCart(productId)
                Log.d("ProductDetailViewModel", "Product added to cart successfully")

                // Show Toast after successfully adding the product
                Toast.makeText(context, "Item added to cart!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("ProductDetailViewModel", "Error adding product to cart: ${e.message}")
                Toast.makeText(context, "Failed to add item to cart", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun syncProductToBlockchain(productId: Int, context: Context) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                val result = productRepository.syncProductToBlockchain(productId)

                if (result != null) {

                    // 🔥 REFRESH FULL PRODUCT
                    val refreshedProduct =
                        productRepository.getProductDetail(productId)

                    _state.value = _state.value.copy(
                        productDetail = refreshedProduct,
                        isLoading = false,
                        errorMessage = ""
                    )

                    Toast.makeText(
                        context,
                        "Product Synced to Ledger!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    errorMessage = e.message ?: "Sync failed",
                    isLoading = false
                )
                Toast.makeText(context, e.message ?: "Sync failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun submitReview(context: Context, review: ReviewRequest) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val token = getJwtToken(context).firstOrNull()
                if (token == null) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "Please login to submit a review"
                    )
                    Toast.makeText(context, "Please login to submit a review", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val response = reviewRepository.submitReview(token, review)
                Log.d("ProductDetailViewModel", "Response: $response")

                // Robust success check: handle different backend response styles
                // We check status, message keywords, and cases where status is false but no error message exists
                val isSuccess = response.status || 
                                response.message?.contains("success", ignoreCase = true) == true ||
                                response.message?.contains("submitted", ignoreCase = true) == true ||
                                response.message?.contains("added", ignoreCase = true) == true ||
                                (response.status == false && response.error == null && response.message != null)

                if (isSuccess) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "" 
                    )
                    Toast.makeText(context, "Review submitted successfully!", Toast.LENGTH_SHORT).show()
                    
                    // Refresh product details, reviews and re-check canReview
                    // This now preserves the current state while fetching updates
                    getProductDetail(review.productId, context)
                } else {
                    val errorMsg = response.message ?: response.error ?: "Failed to submit review"
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = errorMsg
                    )
                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                    Log.e("ProductDetailViewModel", "Error submitting review: $errorMsg")
                }
            } catch (e: Exception) {
                val errorMsg = e.message ?: "An unexpected error occurred"
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = errorMsg
                )
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                Log.e("ProductDetailViewModel", "Exception while submitting review: ${e.message}")
            }
        }
    }
}
