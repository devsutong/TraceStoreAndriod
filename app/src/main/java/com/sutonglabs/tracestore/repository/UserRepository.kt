package com.sutonglabs.tracestore.repository

import android.content.Context
import android.util.Log
import com.sutonglabs.tracestore.api.LoginRequest
import com.sutonglabs.tracestore.api.RegisterRequest
import com.sutonglabs.tracestore.api.TraceStoreAPI
import com.sutonglabs.tracestore.api.request_models.UpdateUserRequest
import com.sutonglabs.tracestore.models.User
import com.sutonglabs.tracestore.data.getJwtToken
import com.sutonglabs.tracestore.data.saveJwtToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val apiService: TraceStoreAPI,
    private val context: Context
) {
    val jwtToken: Flow<String?> = getJwtToken(context)

    suspend fun getUserInfo(token: String): Result<User> {
        val response = apiService.getUserInfo("Bearer $token")

        return if (response.isSuccessful && response.body() != null) {
            val apiUser = response.body()!!.data

            val mappedUser = User(
                id = apiUser.id,
                username = apiUser.username ?: "Unknown",
                email = apiUser.email ?: "No Email",
                firstName = apiUser.firstName ?: "",
                lastName = apiUser.lastName ?: "",
                age = apiUser.age,
                role = apiUser.role ?: "User",
                gstin = apiUser.gstin,
                createdAt = apiUser.createdAt,
                updatedAt = apiUser.updatedAt
            )

            Result.success(mappedUser)
        } else {
            Result.failure(Exception("Failed to fetch user info"))
        }
    }

    suspend fun getUser(): Result<Int> {
        val token = getJwtToken(context).first()
        val response = apiService.getUser("Bearer $token")
        return if (response.isSuccessful && response.body() != null) {
            Result.success(response.body()!!.data.id)
        } else {
            Result.failure(Exception("Failed to fetch user"))
        }
    }

    suspend fun login(username: String, password: String): Result<String> {
        val response = apiService.login(LoginRequest(username, password))
        return if (response.isSuccessful && response.body() != null) {
            val responseData = response.body()!!
            val jwt = responseData.data.token
            val role = responseData.data.user.role

            saveJwtToken(context, jwt)
            saveUserRole(context, role)

            Log.d("UserRepository", "Saved JWT: $jwt | Role: $role")
            Result.success(jwt)
        } else {
            Result.failure(Exception("Login failed"))
        }
    }

    private fun saveUserRole(context: Context, role: String) {
        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            .edit()
            .putString("USER_ROLE", role)
            .apply()
    }

    suspend fun register(
        username: String,
        email: String,
        firstName: String,
        lastName: String,
        age: Int,
        GSTIN: String,
        password: String
    ): Result<String> {
        val request = RegisterRequest(username, email, firstName, lastName, age, GSTIN, password)
        val response = apiService.register(request)

        return if (response.isSuccessful && response.body() != null) {
            val jwt = response.body()!!.data.token
            saveJwtToken(context, jwt)
            Log.d("UserRepository", "Saved JWT Token: $jwt")
            Result.success(jwt)
        } else {
            Result.failure(Exception("Registration failed!"))
        }
    }

    suspend fun clearJwtToken(context: Context) {
        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
        Log.d("UserRepository", "JWT Token cleared.")
    }

    suspend fun updateUser(token: String, firstName: String, lastName: String, age: Int): Result<User> {
        val request = UpdateUserRequest(firstName, lastName, age)
        val response = apiService.updateUser(request, "Bearer $token")

        return if (response.isSuccessful && response.body() != null) {
            Log.d("UserRepository", "User profile updated: ${response.body()!!.data}")
            Result.success(response.body()!!.data)
        } else {
            Log.d("UserRepository", "Failed to update user profile")
            Result.failure(Exception("Update failed"))
        }
    }

    suspend fun getAllUsers(token: String): Result<List<User>> {
        return try {
            val response = apiService.getAllUsers("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                val apiUsers = response.body()!!.data
                Result.success(apiUsers)
            } else {
                Result.failure(Exception("Failed to fetch users"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
