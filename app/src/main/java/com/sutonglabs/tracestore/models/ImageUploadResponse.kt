package com.sutonglabs.tracestore.models

data class ImageUploadResponse(
    val status: Boolean,
    val message: String,
    val image_uuids : List<String>
)
