package com.sutonglabs.tracestore.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sutonglabs.tracestore.common.Constants
import com.sutonglabs.tracestore.models.ProductImage

@Composable
fun ProductImageCarousel(
    images: List<ProductImage>
) {
    if (images.isEmpty()) return

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(images) { image ->
            AsyncImage(
                model = Constants.BASE_URL + image.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillParentMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}
