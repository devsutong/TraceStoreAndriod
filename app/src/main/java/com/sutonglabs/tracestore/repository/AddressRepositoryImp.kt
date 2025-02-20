package com.sutonglabs.tracestore.repository

import android.content.Context
import android.util.Log
import com.sutonglabs.tracestore.api.TraceStoreAPI
import com.sutonglabs.tracestore.api.request_models.CreateAddressRequest
import com.sutonglabs.tracestore.api.request_models.UpdateAddressRequest
import com.sutonglabs.tracestore.api.response_model.CreateAddressResponse
import com.sutonglabs.tracestore.api.response_model.UpdateAddressResponse
import com.sutonglabs.tracestore.data.getJwtToken
import com.sutonglabs.tracestore.models.AddressResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.await
import javax.inject.Inject

class AddressRepositoryImp @Inject constructor(
    private val traceStoreApiService: TraceStoreAPI,
): AddressRepository {
    override suspend fun getAddress(context: Context): AddressResponse {
        val token = getJwtToken(context).first()
        return withContext(Dispatchers.IO) {
            traceStoreApiService.getAddress("Bearer $token").await()
        }
    }

    override suspend fun createAddress(context: Context, address: CreateAddressRequest): CreateAddressResponse {
        val token = getJwtToken(context).first()
        return withContext(Dispatchers.IO) {
            val response = traceStoreApiService.createAddress("Bearer $token", address)
            if (response.isSuccessful && response.body() != null) {
                Log.d("AddressRepository", "Address created successfully")
                response.body()!!
            } else {
                throw Exception("Failed to create Address. ${response.errorBody()?.string()}")
            }
        }
    }

    override suspend fun updateAddress(
        context: Context,
        updatedAddress: UpdateAddressRequest
    ): UpdateAddressResponse {
        val token = getJwtToken(context).first()
        return withContext(Dispatchers.IO) {
            val response = traceStoreApiService.updateAddress("Bearer $token", updatedAddress)
            if (response.isSuccessful && response.body() != null) {
                Log.d("AddressRepository", "Address updated successfully")
                response.body()!!
            } else {
                throw Exception("Failed to updated Address. ${response.errorBody()?.string()}")
            }
        }
    }
}