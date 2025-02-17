package com.sutonglabs.tracestore.models

data class ProductCreate(
    val name: String,
    val description: String,
    var image: String,
    val price: Int,
    val priceUnit: String = "inr",
    val categoryIds: List<Int>
)
