package com.sutonglabs.tracestore.repository

import com.sutonglabs.tracestore.api.TraceStoreAPI
import com.sutonglabs.tracestore.common.CategoryCache
import com.sutonglabs.tracestore.models.Category

import android.util.Log

class CategoryRepository(
    private val traceStoreApiService: TraceStoreAPI
) {

    suspend fun getCategories(): List<Category> {

        CategoryCache.get()?.let {
            Log.d("CategoryRepository", "Returning categories from CACHE")
            return it
        }

        return try {
            Log.d("CategoryRepository", "Fetching categories from BACKEND")
            val categories = traceStoreApiService.getCategories()
            Log.d("CategoryRepository", "Fetched ${categories.size} categories")

            CategoryCache.set(categories)
            categories
        } catch (e: Exception) {
            Log.e("CategoryRepository", "Failed to fetch categories", e)
            emptyList()
        }
    }
}
