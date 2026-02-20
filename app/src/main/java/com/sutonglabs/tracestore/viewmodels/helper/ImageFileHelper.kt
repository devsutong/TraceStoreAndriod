package com.sutonglabs.tracestore.viewmodels.helper

import android.content.Context
import android.net.Uri
import java.io.File

object ImageFileHelper {

    fun uriToFile(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Unable to open URI: $uri")

        val tempFile = File.createTempFile(
            "upload_",
            ".jpg",
            context.cacheDir
        )

        inputStream.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return tempFile
    }
}