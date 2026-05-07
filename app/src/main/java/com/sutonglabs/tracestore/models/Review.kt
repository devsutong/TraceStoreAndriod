package com.sutonglabs.tracestore.models

import com.google.gson.annotations.SerializedName

data class Review(
    val id: Int,
    val productId: Int,
    val userId: Int,
    val rating: Int,
    val reviewText: String,
    val createdAt: String,
    @SerializedName("User")
    val User: UserInfo? = null
)

data class UserInfo(
    val firstName: String,
    val lastName: String,
    val profileImage: String? = null
)

data class ReviewResponse(
    val count: Int,
    val rows: List<Review> = emptyList()
)
