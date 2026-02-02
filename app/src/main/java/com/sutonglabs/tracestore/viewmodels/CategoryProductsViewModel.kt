package com.sutonglabs.tracestore.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sutonglabs.tracestore.models.Product
import com.sutonglabs.tracestore.services.RetrofitInstance
import kotlinx.coroutines.launch
import android.util.Log

class CategoryProductsViewModel : ViewModel() {

    var products by mutableStateOf<List<Product>>(emptyList())
        private set

    var loading by mutableStateOf(false)
        private set

    fun load(categoryId: Int) {
        viewModelScope.launch {
            loading = true
            try {
                val response = RetrofitInstance.api.getProductsByCategory(categoryId)
                if (response.isSuccessful) {
                    products = response.body()?.data ?: emptyList()
                    Log.d("CategoryProductsVM", "Loaded ${products.size} products")
                } else {
                    products = emptyList()
                }
            } catch (e: Exception) {
                Log.e("CategoryProductsVM", "Error", e)
                products = emptyList()
            } finally {
                loading = false
            }
        }
    }
}