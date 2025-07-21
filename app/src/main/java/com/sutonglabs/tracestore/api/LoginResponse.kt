package com.sutonglabs.tracestore.api

data class LoginResponse(
    val status: Boolean,
    val data: LoginResponseData
)

data class LoginResponseData(
    val user: User,
    val token: String
)

data class GetUserResponse(
    val status: Boolean,
    val data: User,
)

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val age: String,
    val role: String,
    val gstin: String,
    val createdAt: String,
    val updatedAt: String
)