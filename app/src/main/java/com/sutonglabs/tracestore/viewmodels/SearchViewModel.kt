package com.sutonglabs.tracestore.viewmodels
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sutonglabs.tracestore.models.Product
import com.sutonglabs.tracestore.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


@HiltViewModel

class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
) : ViewModel() {

    var products by mutableStateOf<List<Product>>(emptyList())
        private set

    var page = 1
        private set

    private val limit = 5
    private var canLoadMore = true

    fun search(query: String) {
        page = 1
        canLoadMore = true
        products = emptyList()
        loadMore(query)
    }

    fun loadMore(query: String) {
        if (!canLoadMore) return

        viewModelScope.launch {
            val response = repository.search(query, page, limit)

            products = products + response.results

            val loaded = page * limit
            if (loaded >= response.total) {
                canLoadMore = false
            } else {
                page++
            }
        }
    }
}
