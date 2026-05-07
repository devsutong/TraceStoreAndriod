package com.sutonglabs.tracestore.repository

import com.sutonglabs.tracestore.api.request_models.ReviewRequest
import com.sutonglabs.tracestore.api.response_model.ApiResponse
import com.sutonglabs.tracestore.api.response_model.CanReviewResponse
import com.sutonglabs.tracestore.models.ReviewResponse
import com.sutonglabs.tracestore.services.ReviewService
import javax.inject.Inject

class ReviewRepository @Inject constructor(
    private val api: ReviewService
) {

    suspend fun submitReview(token: String, review: ReviewRequest): ApiResponse {
        return api.submitReview("Bearer $token", review)
    }

    suspend fun canReview(token: String, productId: Int): CanReviewResponse {
        return api.canReview("Bearer $token", productId)
    }

    suspend fun getReviews(productId: Int): ReviewResponse {
        return api.getReviews(productId)
    }
}
