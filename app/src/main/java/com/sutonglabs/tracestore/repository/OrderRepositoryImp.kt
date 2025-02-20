package com.sutonglabs.tracestore.repository

import android.content.Context
import com.sutonglabs.tracestore.api.TraceStoreAPI
import com.sutonglabs.tracestore.models.Order
import com.sutonglabs.tracestore.api.request_models.CreateOrderRequest
import com.sutonglabs.tracestore.api.response_model.CreateOrderResponse
import com.sutonglabs.tracestore.data.getJwtToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepositoryImp @Inject constructor(
    private val traceStoreApiService: TraceStoreAPI
) : OrderRepository {

    override suspend fun createOrder(context: Context, orderRequest: CreateOrderRequest): CreateOrderResponse {
        val token = getJwtToken(context).first()
        return withContext(Dispatchers.IO) {
            val response = traceStoreApiService.createOrder("Bearer $token", orderRequest)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                throw Exception("Failed to create order. ${response.errorBody()?.string()}")
            }
        }
    }

    override suspend fun getOrders(context: Context): List<Order> {
        val token = getJwtToken(context).first()
        return withContext(Dispatchers.IO) {
            val response = traceStoreApiService.getOrders("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                throw Exception("Failed to fetch orders. ${response.errorBody()?.string()}")
            }
        }
    }
}
