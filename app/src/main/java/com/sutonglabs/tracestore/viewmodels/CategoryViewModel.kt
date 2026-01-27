package com.sutonglabs.tracestore.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sutonglabs.tracestore.common.CategoryCache
import com.sutonglabs.tracestore.models.Category
import com.sutonglabs.tracestore.services.RetrofitInstance
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import android.util.Log


class CategoryViewModel : ViewModel() {

    var categories by mutableStateOf<List<Category>>(emptyList())
        private set

    init {
        Log.d("CategoryVM", "ViewModel CREATED")
        loadOnce()
    }

    private fun loadOnce() {
        viewModelScope.launch {

            Log.d("CategoryVM", "loadOnce() called")

            // 1️⃣ Cache check
            CategoryCache.get()?.let {
                Log.d("CategoryVM", "Using cached categories")
                categories = it
                return@launch
            }

            // 2️⃣ Backend fetch
            Log.d("CategoryVM", "Fetching categories from BACKEND")

            try {
                val result = RetrofitInstance.api.getCategories()
                Log.d("CategoryVM", "Backend returned ${result.size} categories")

                CategoryCache.set(result)
                categories = result

            } catch (e: Exception) {
                Log.e("CategoryVM", "Failed to fetch categories", e)
            }
        }
    }
}


