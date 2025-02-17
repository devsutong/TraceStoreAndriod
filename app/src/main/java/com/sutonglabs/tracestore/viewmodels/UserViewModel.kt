package com.sutonglabs.tracestore.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sutonglabs.tracestore.models.User
import androidx.lifecycle.viewmodel.compose.viewModel
//import com.sutonglabs.tracestore.api.User
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

    val jwtToken: StateFlow<String?> = userRepository.jwtToken
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

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

    init {
        viewModelScope.launch {
            setUser()
        }
    }
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
}
