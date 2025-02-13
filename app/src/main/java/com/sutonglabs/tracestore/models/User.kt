package com.sutonglabs.tracestore.models

data class User(
    val id: Int,
    val username: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val age: Int = 0,
    val role: String = "",
    val gstin: String? = null,
    val createdAt: String = "",
    val updatedAt: String = ""
)

data class LoginUser(
    val username: String = "",
    val password: String = ""
)

data class UserResponse(
    val status: Boolean,
    val data: User
)
