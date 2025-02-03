package com.sutonglabs.tracestore.api

import com.sutonglabs.tracestore.models.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    @GET("user")
    suspend fun getUserInfo(@Header("Authorization") token: String): Response<User>

    @POST("user/update")
    suspend fun updateUser(@Body user: User, @Header("Authorization") token: String): Response<User>

    @DELETE("user/{id}")
    suspend fun deleteUser(@Path("id") id: Int, @Header("Authorization") token: String): Response<Unit>

    @GET("cart")
    fun getCart(@Header("Authorization") token: String): Call<CartResponse>

    @GET("address")
    fun getAddress(@Header("Authorization") token: String): Call<AddressResponse>

    @POST("/cart/add")
    suspend fun addToCart(
        @Body request: AddToCartRequest,
        @Header("Authorization") token: String
    ): Response<CartResponse>

    @POST("product/")
    suspend fun addProduct(
        @Header("Authorization") authHeader: String,  // Add Authorization header
        @Body product: ProductCreate
    ): Response<Product>

    @Multipart
    @POST("upload/image")
    suspend fun uploadImage(@Part image: MultipartBody.Part): Response<ImageUploadResponse>
}

