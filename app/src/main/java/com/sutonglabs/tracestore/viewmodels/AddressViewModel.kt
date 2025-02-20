package com.sutonglabs.tracestore.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sutonglabs.tracestore.api.request_models.CreateAddressRequest
import com.sutonglabs.tracestore.api.request_models.UpdateAddressRequest
import com.sutonglabs.tracestore.api.response_model.CreateAddressResponse
import com.sutonglabs.tracestore.common.Resource
import com.sutonglabs.tracestore.data.getJwtToken
import com.sutonglabs.tracestore.repository.AddressRepository
import com.sutonglabs.tracestore.use_case.CreateAddressUseCase
import com.sutonglabs.tracestore.use_case.GetAddressUseCase
import com.sutonglabs.tracestore.viewmodels.state.AddressState
import com.sutonglabs.tracestore.viewmodels.state.CreateAddressState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val getAddressUseCase: GetAddressUseCase,
    private val addressRepository: AddressRepository,
    ): ViewModel() {
    private val _state = mutableStateOf(AddressState())
    val state: State<AddressState> = _state

    init {
        getAddress()
    }

    private fun getAddress() {
        getAddressUseCase().onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _state.value = AddressState(isLoading = true)
                }

                is Resource.Success -> {
                    _state.value = AddressState(address = result.data?.data ?: emptyList())
                }

                is Resource.Error -> {
                    _state.value = AddressState(errorMessage = result.message ?: "An unexpected error occurred")
                }
            }
        }.launchIn(viewModelScope)
    }

//    private fun updateAddress(addressId: Int, updatedAddress: CreateAddressRequest) {
//        updateAddressUseCase(addressId, updatedAddress).onEach { result ->
//            when(result) {
//                is Resource.Loading -> {
//                    _state.value = AddressState(isLoading = true)
//                }
//                is Resource.Success -> {
//                    // Optionally, update the state with the newly returned address data.
//                    // For example, if your API returns the updated address, you can use it:
//                    _state.value = AddressState(address = result.data?.data ?: emptyList())
//                    // Alternatively, you could trigger a refresh of the address list.
//                }
//                is Resource.Error -> {
//                    _state.value = AddressState(
//                        errorMessage = result.message ?: "An unexpected error occurred"
//                    )
//                }
//            }
//        }.launchIn(viewModelScope)
//    }

    fun updateAddress(updatedaddressRequest: UpdateAddressRequest, context: Context) {
        viewModelScope.launch {
            try {
                addressRepository.updateAddress(context, updatedaddressRequest)
                Log.d("AddressViewModel", "Address updated successfully")

                // Show Toast after successfully adding the product
                Toast.makeText(context, "Address Updated!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("AddressViewModel", "Error Updating address: ${e.message}")
            }
        }
    }

    fun createAddress(addAddressRequest: CreateAddressRequest, context: Context) {
        viewModelScope.launch {
            try {
                addressRepository.createAddress(context, addAddressRequest)
                Log.d("AddressViewModel", "Address created successfully")

                // Show Toast after successfully adding the product
                Toast.makeText(context, "Address Created!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("AddressViewModel", "Error creating address: ${e.message}")
            }
        }
    }


}