package com.sutonglabs.tracestore.common
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val JWT_TOKEN = stringPreferencesKey("jwt_token")
}


object Constants {
    // API Constants
    const val BASE_URL = "http://10.0.2.2:3000/"
    const val TIMEOUT_DURATION = 30L
    const val API_KEY = "your-api-key"
    const val DATABASE_NAME = "my_database"
}