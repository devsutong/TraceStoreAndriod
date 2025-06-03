package com.sutonglabs.tracestore.repository

import android.content.Context
import com.sutonglabs.tracestore.api.request_models.CreateOrderRequest
import com.sutonglabs.tracestore.api.response_model.CreateOrderResponse
import com.sutonglabs.tracestore.api.response_model.Order
import com.sutonglabs.tracestore.models.SellerOrderResponse


interface OrderRepository {
    suspend fun createOrder(context: Context, orderRequest: CreateOrderRequest): CreateOrderResponse
    suspend fun getOrders(context: Context): List<Order>
    suspend fun getSellerOrders(context: Context): List<SellerOrderResponse>  // changed here
}

