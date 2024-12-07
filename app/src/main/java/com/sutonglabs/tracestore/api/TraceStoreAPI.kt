package com.sutonglabs.tracestore.api

import com.sutonglabs.tracestore.models.AddToCartRequest
import com.sutonglabs.tracestore.models.CartResponse
import com.sutonglabs.tracestore.models.ProductResponse
import com.sutonglabs.tracestore.models.Product
import com.sutonglabs.tracestore.models.ProductDetailResponse
import retrofit2.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

data class LoginRequest(val username: String, val password: String)
data class RegisterRequest(val username: String, val email: String, val firstName: String, val lastName: String, val age: String, val GSTIN: String, val password: String)

interface TraceStoreAPI {
    @POST("signup")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("product")
    suspend fun getProducts(): Response<ProductResponse>

    @GET("product/{id}")
    suspend fun getProductDetail(
        @Path("id") id: Int,
        @Header("Authorization") authHeader: String
    ): Response<ProductDetailResponse>

    @GET("cart")
    fun getCart(@Header("Authorization") token: String): Call<CartResponse>

    @POST("/cart/add")
    suspend fun addToCart(
        @Body request: AddToCartRequest,
        @Header("Authorization") token: String
    ): Response<CartResponse>


}
