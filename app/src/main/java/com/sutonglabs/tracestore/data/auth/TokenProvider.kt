package com.sutonglabs.tracestore.data.auth

import android.content.Context
import com.sutonglabs.tracestore.data.getJwtToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TokenProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun getToken(): String? {
        return getJwtToken(context).first()
    }
}
