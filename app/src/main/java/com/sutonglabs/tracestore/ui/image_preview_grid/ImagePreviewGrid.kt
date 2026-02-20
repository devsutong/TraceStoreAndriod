package com.sutonglabs.tracestore.ui.image_preview_grid

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import coil.compose.AsyncImage

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ImagePreviewGrid(
    imageUris: List<Uri>,
    onRemove: (Uri) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        imageUris.forEach { uri ->
            Box(
                modifier = Modifier.size(100.dp)
            ) {
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )

                IconButton(
                    onClick = { onRemove(uri) },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove image"
                    )
                }
            }
        }
    }
}
