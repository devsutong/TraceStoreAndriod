package com.sutonglabs.tracestore.common

import com.sutonglabs.tracestore.models.Category
import android.util.Log


object CategoryCache {

    private var categories: List<Category>? = null

    fun get(): List<Category>? {
        Log.d("CategoryCache", "get() called. Cached = ${categories != null}")
        return categories
    }

    fun set(data: List<Category>) {
        Log.d("CategoryCache", "set() called. Size = ${data.size}")
        categories = data
    }

    fun clear() {
        Log.d("CategoryCache", "clear() called")
        categories = null
    }
}

