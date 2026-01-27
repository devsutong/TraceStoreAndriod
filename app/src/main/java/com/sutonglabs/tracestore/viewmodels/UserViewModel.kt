package com.sutonglabs.tracestore.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sutonglabs.tracestore.models.User
import com.sutonglabs.tracestore.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userInfo = MutableStateFlow<User?>(null)
    val userInfo: StateFlow<User?> = _userInfo

    private val _user = MutableStateFlow<Result<Int>?>(null)
    val user: StateFlow<Result<Int>?> = _user

    private val _loginState = MutableStateFlow<Result<String>?>(null)
    private val _registerState = MutableStateFlow<Result<String>?>(null)
    private val _updateState = MutableStateFlow<Result<User>?>(null)
    private val _deleteState = MutableStateFlow<Result<Unit>?>(null)

    val loginState: StateFlow<Result<String>?> = _loginState
    val registerState: StateFlow<Result<String>?> = _registerState
    val updateState: StateFlow<Result<User>?> = _updateState
    val deleteState: StateFlow<Result<Unit>?> = _deleteState

    // JWT Token flow from UserRepository
    val jwtToken: StateFlow<String?> = userRepository.jwtToken
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    init {
        viewModelScope.launch {
            // Check if JWT Token is being emitted properly
            jwtToken.collect { token ->
                Log.d("UserViewModel", "JWT Token collected: $token")

                if (!token.isNullOrBlank()) {
                    fetchUserInfo(token)
                }
            }
        }
        // Fetch user data after initialization
//        viewModelScope.launch {
//            setUser()
//        }
    }
    fun fetchUserInfo(token: String) {
        viewModelScope.launch {
            val result = userRepository.getUserInfo(token)
            result.onSuccess { user ->
                _userInfo.value = user
            }.onFailure {
                _userInfo.value = null
            }
        }
    }
    // Set user data
    private fun setUser() {
        viewModelScope.launch {
            _user.value = userRepository.getUser()
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = userRepository.login(username, password)
        }
    }

    fun register(
        username: String,
        email: String,
        firstName: String,
        lastName: String,
        age: String,
        GSTIN: String,
        password: String
    ) {
        viewModelScope.launch {
            _registerState.value = userRepository.register(username, email, firstName, lastName, age, GSTIN, password)
        }
    }

    // Function to update the user profile
    fun updateUserProfile(firstName: String, lastName: String, age: Int) {
        val token = jwtToken.value

        // Log token status before performing update
        if (token.isNullOrEmpty()) {
            Log.d("UserViewModel", "JWT Token is null or empty!")
        } else {
            Log.d("UserViewModel", "Using JWT Token for update: $token")

            viewModelScope.launch {
                val result = userRepository.updateUser(token, firstName, lastName, age)
                _updateState.value = result
            }
        }
    }
    fun logout(context: Context) {
        viewModelScope.launch {
            // Clear the token from the repository or local storage
            userRepository.clearJwtToken(context)
        }
    }


    // Reset update state
    fun resetUpdateState() {
        _updateState.value = null
    }
    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing

    // Inside UserViewModel.kt
    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    fun syncAndRegister() {
        val token = jwtToken.value
        if (token.isNullOrEmpty()) return

        viewModelScope.launch {
            val result = userRepository.syncUserToBlockchain(token)

            result.onSuccess { updatedUser ->
                _userInfo.value = updatedUser
            }.onFailure {
                // We set a simple error flag or just rely on the UI logic
                _userInfo.value = _userInfo.value?.copy(blockchainStatus = false)
            }
        }
    }

}
