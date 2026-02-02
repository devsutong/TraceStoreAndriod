package com.sutonglabs.tracestore.ui.home_screen.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sutonglabs.tracestore.models.Category

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryStrip(
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit
) {
    if (categories.isEmpty()) return

    LazyHorizontalGrid(
        rows = GridCells.Fixed(1),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(start = 6.dp, top = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories.size) { index ->
            CategoryItem(
                category = categories[index],
                onClick = { onCategoryClick(categories[index]) }
            )
        }
    }
}
