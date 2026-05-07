package com.sutonglabs.tracestore.repository

import android.util.Log
import com.sutonglabs.tracestore.api.TraceStoreAPI
import com.sutonglabs.tracestore.api.response_model.pincode_response.PincodeResponse
import javax.inject.Inject

class PincodeRepository @Inject constructor(
    private val api: TraceStoreAPI
) {

    suspend fun fetchLocation(
        pincode: String
    ): PincodeResponse? {
        return try {
            api.getLocationFromPincode(pincode)
        } catch (e: Exception) {
            Log.e("PincodeRepository", "fetchLocation Exception", e)
            null
        }
    }
}
