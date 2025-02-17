package com.sutonglabs.tracestore.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sutonglabs.tracestore.repository.ProductRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.sutonglabs.tracestore.models.ImageUploadResponse
import com.sutonglabs.tracestore.models.ProductCreate
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody

class AddProductViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private val _addProductStatus = mutableStateOf<Boolean?>(null)
    val addProductStatus: State<Boolean?> = _addProductStatus

    fun addProduct(product: ProductCreate, context: Context) {
        viewModelScope.launch {
            val result = productRepository.addProduct(product, context) // Pass context to repository
            _addProductStatus.value = result != null
        }
    }


    // New method to upload an image and create a product with the uploaded image path
    fun uploadImage(image: MultipartBody.Part): ImageUploadResponse? {
        return runBlocking {
            try {
                val imageUploadResponse = productRepository.uploadImage(image)
                return@runBlocking imageUploadResponse
            } catch (e: Exception) {
                Log.e("AddProductViewModel", "Error uploading image: ${e.localizedMessage}")
                return@runBlocking null
            }
        }
    }
}
