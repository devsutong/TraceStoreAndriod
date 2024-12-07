package com.sutonglabs.tracestore.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import android.util.Base64
import org.json.JSONObject

suspend fun saveJwtToken(context: Context, token: String) {
    context.dataStore.edit { preferences ->
        preferences[PreferencesKeys.JWT_TOKEN] = token
    }
}

fun getJwtToken(context: Context): Flow<String?> {
    return context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.JWT_TOKEN]
        }
}

fun decodeJwt(token: String): JSONObject {
    try {
        val parts = token.split(".")
        if (parts.size != 3) throw IllegalArgumentException("Invalid JWT token format")
        val payload = String(Base64.decode(parts[1], Base64.URL_SAFE), Charsets.UTF_8)
        return JSONObject(payload) // Convert payload into a JSONObject for easy parsing
    } catch (e: Exception) {
        throw IllegalArgumentException("Failed to decode JWT: ${e.message}")
    }
}
