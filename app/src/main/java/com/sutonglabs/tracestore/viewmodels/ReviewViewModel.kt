package com.sutonglabs.tracestore.viewmodels

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sutonglabs.tracestore.api.request_models.ReviewRequest
import com.sutonglabs.tracestore.data.getJwtToken
import com.sutonglabs.tracestore.repository.ReviewRepository
import com.sutonglabs.tracestore.viewmodels.state.ReviewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val repository: ReviewRepository
) : ViewModel() {

    private val _state = mutableStateOf(ReviewState())
    val state: State<ReviewState> = _state

    fun submitReview(context: Context, review: ReviewRequest) {
        viewModelScope.launch {
            _state.value = ReviewState(isLoading = true)
            try {
                val token = getJwtToken(context).firstOrNull()
                if (token == null) {
                    _state.value = ReviewState(error = "Please login to submit a review")
                    return@launch
                }

                val response = repository.submitReview(token, review)

                if (response.status) {
                    _state.value = ReviewState(isSuccess = true)
                } else {
                    _state.value = ReviewState(error = response.message ?: response.error ?: "Failed to submit review")
                }

            } catch (e: Exception) {
                _state.value = ReviewState(error = e.message ?: "An unexpected error occurred")
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
