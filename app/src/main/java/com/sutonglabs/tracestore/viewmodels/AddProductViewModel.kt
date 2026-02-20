package com.sutonglabs.tracestore.viewmodels

import android.content.Context
import android.net.Uri
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
import com.sutonglabs.tracestore.viewmodels.state.AddProductState

class AddProductViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _state = mutableStateOf<AddProductState>(AddProductState.Idle)
    val state: State<AddProductState> = _state

    fun createProduct(
        context: Context,
        product: ProductCreate,
        imageUris: List<Uri>
    ) {
        viewModelScope.launch {
            _state.value = AddProductState.Loading
            try {
                val uploadResponse =
                    productRepository.uploadImages(context, imageUris)

                val finalProduct =
                    product.copy(image_uuids = uploadResponse.image_uuids)

                productRepository.addProduct(finalProduct)
                _state.value = AddProductState.Success

            } catch (e: Exception) {
                _state.value =
                    AddProductState.Error(e.message ?: "Something went wrong")
            }
        }
    }

}
