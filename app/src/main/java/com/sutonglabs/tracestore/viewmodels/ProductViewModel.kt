package com.sutonglabs.tracestore.viewmodels

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sutonglabs.tracestore.models.Product
import com.sutonglabs.tracestore.models.ProductCreate
import com.sutonglabs.tracestore.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val selectedProduct: Product? = null
)

@HiltViewModel
class ProductViewModel @Inject constructor(
    application: Application,
    private val repository: ProductRepository
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()
    var updateStatus by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun fetchProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val response = repository.getProduct()
            _uiState.update {
                it.copy(
                    products = response.data,
                    isLoading = false
                )
            }
        }
    }

    fun updateProduct(id: Int, updatedProduct: ProductCreate, context: Context) {
        viewModelScope.launch {
            try {
                val result = repository.updateProduct(id, updatedProduct, context)
                updateStatus = result != null
                errorMessage = if (result == null) "Update failed" else null
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
                updateStatus = false
            }
        }
    }
        fun deleteProduct(productId: Int) {
                viewModelScope.launch {
                    val context = getApplication<Application>().applicationContext
                    repository.deleteProduct(productId, context)
                    fetchProducts()
                }
        }

        fun selectProductForUpdate(product: Product) {
                _uiState.update { it.copy(selectedProduct = product) }
        }
    }

