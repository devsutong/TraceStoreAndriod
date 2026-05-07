package com.sutonglabs.tracestore.api.response_model.pincode_response

data class PincodeResponse(
    val success: Boolean,
    val data: LocationData
)

data class LocationData(
    val city: String,
    val state: String,
    val country: String
)