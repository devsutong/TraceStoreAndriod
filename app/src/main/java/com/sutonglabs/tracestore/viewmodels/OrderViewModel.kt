package com.sutonglabs.tracestore.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sutonglabs.tracestore.api.request_models.CreateOrderRequest
import com.sutonglabs.tracestore.api.response_model.Order
import com.sutonglabs.tracestore.models.SellerOrderResponse
import com.sutonglabs.tracestore.repository.OrderRepository
import com.sutonglabs.tracestore.viewmodels.state.CreateOrderState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _order = MutableStateFlow<List<Order>>(emptyList())
    val order: StateFlow<List<Order>> = _order

    private val _state = mutableStateOf(CreateOrderState())
    val state: State<CreateOrderState> = _state

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun createOrder(orderRequest: CreateOrderRequest, context: Context) {
        viewModelScope.launch {
            _state.value = CreateOrderState(isLoading = true)
            try {
                orderRepository.createOrder(context, orderRequest)
                _state.value = CreateOrderState(isSuccess = true)
                Log.d("OrderViewModel", "Order created successfully")
            } catch (e: Exception) {
                _state.value = CreateOrderState(errorMessage = e.message ?: "Unknown error")

                Log.e("OrderViewModel", "Error creating order: ${e.message}")
            }
        }
    }

    fun fetchOrders(context: Context) {
        viewModelScope.launch {
            try {
                val response = orderRepository.getOrders(context)
                _order.value = response
                Log.d("OrderViewModel", "Orders fetched successfully: ${response.size} order")
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e("OrderViewModel", "Error fetching order: ${e.message}")
            }
        }
    }

    private val _sellerOrders = MutableStateFlow<List<SellerOrderResponse>>(emptyList())
    val sellerOrders: StateFlow<List<SellerOrderResponse>> = _sellerOrders

    fun fetchSellerOrders(context: Context) {
        viewModelScope.launch {
            try {
                val response = orderRepository.getSellerOrders(context)
                _sellerOrders.value = response
                Log.d("OrderViewModel", "Seller orders fetched: ${response.size}")
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e("OrderViewModel", "Error fetching seller orders: ${e.message}")
            }
        }
    }



}

