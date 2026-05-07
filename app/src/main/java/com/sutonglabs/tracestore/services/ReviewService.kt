package com.sutonglabs.tracestore.services

import com.sutonglabs.tracestore.api.request_models.ReviewRequest
import com.sutonglabs.tracestore.api.response_model.ApiResponse
import com.sutonglabs.tracestore.api.response_model.CanReviewResponse
import com.sutonglabs.tracestore.models.ReviewResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewService {

    @POST("review")
    suspend fun submitReview(
        @Header("Authorization") token: String,
        @Body review: ReviewRequest
    ): ApiResponse

    @GET("review/{productId}/can-review")
    suspend fun canReview(
        @Header("Authorization") token: String,
        @Path("productId") productId: Int
    ): CanReviewResponse

    @GET("review/{productId}")
    suspend fun getReviews(
        @Path("productId") productId: Int
    ): ReviewResponse
}
