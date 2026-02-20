package com.sutonglabs.tracestore.models

data class ProductCreate(
    val name: String,
    val description: String,
    val image_uuids: List<String>,
    val price: Int,
    val priceUnit: String = "inr",
    val categoryIds: List<Int>
)
