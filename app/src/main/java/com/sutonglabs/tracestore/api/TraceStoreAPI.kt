package com.sutonglabs.tracestore.api

import com.sutonglabs.tracestore.models.*
import okhttp3.MultipartBody
import android.util.Log
import com.sutonglabs.tracestore.api.request_models.CreateAddressRequest
import com.sutonglabs.tracestore.api.request_models.CreateOrderRequest
import com.sutonglabs.tracestore.api.request_models.UpdateAddressRequest
import com.sutonglabs.tracestore.api.response_model.CreateAddressResponse
import com.sutonglabs.tracestore.api.response_model.CreateOrderResponse
import com.sutonglabs.tracestore.api.response_model.UpdateAddressResponse
import com.sutonglabs.tracestore.data.getJwtToken
import com.sutonglabs.tracestore.models.AddToCartRequest
import com.sutonglabs.tracestore.models.AddressResponse
import com.sutonglabs.tracestore.models.CartResponse
import com.sutonglabs.tracestore.models.ProductResponse
import com.sutonglabs.tracestore.models.ProductDetailResponse
import kotlinx.coroutines.flow.first
import retrofit2.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PUT
import retrofit2.http.Path

data class LoginRequest(val username: String, val password: String)
data class RegisterRequest(val username: String, val email: String, val firstName: String, val lastName: String, val age: String, val GSTIN: String, val password: String)
data class UpdateCartRequest(val cartItemId: Int, val quantity: Int)

interface TraceStoreAPI {
    @POST("signup")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("user")
    suspend fun getUser(@Header("Authorization") token: String): Response<GetUserResponse>

    @GET("product")
    suspend fun getProducts(): Response<ProductResponse>

    @GET("product/{id}")
    suspend fun getProductDetail(
        @Path("id") id: Int,
        @Header("Authorization") authHeader: String
    ): Response<ProductDetailResponse>

    @GET("user")
    suspend fun getUserInfo(@Header("Authorization") token: String): Response<UserResponse>

    @POST("user/update")
    suspend fun updateUser(@Body user: User, @Header("Authorization") token: String): Response<User>

    @DELETE("user/{id}")
    suspend fun deleteUser(@Path("id") id: Int, @Header("Authorization") token: String): Response<Unit>

    @GET("cart")
    fun getCart(@Header("Authorization") token: String): Call<CartResponse>

    @GET("address")
    fun getAddress(@Header("Authorization") token: String): Call<AddressResponse>

    @POST("address")
    suspend fun createAddress(
        @Header("Authorization") token: String,
        @Body address: CreateAddressRequest
    ): Response<CreateAddressResponse>

    @PUT("address")
    suspend fun updateAddress(
        @Header("Authorization") token: String,
        @Body address: UpdateAddressRequest
    ): Response<UpdateAddressResponse>

    @POST("/cart/add")
    suspend fun addToCart(
        @Body request: AddToCartRequest,
        @Header("Authorization") token: String
    ): Response<CartResponse>

    @POST("order")
    suspend fun createOrder(
        @Header("Authorization") token: String,
        @Body address: CreateOrderRequest
    ): Response<CreateOrderResponse>

    @PUT("cart/update")
    suspend fun updateCartItem(
        @Header("Authorization") token: String,
        @Body request: UpdateCartRequest
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

