package com.sutonglabs.tracestore.ui.home_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sutonglabs.tracestore.common.category.CategoryIconMapper
import com.sutonglabs.tracestore.models.Category

@Composable
fun CategoryItem(
    category: Category,
    onClick: () -> Unit
) {
    val iconRes = remember(category.name) {
        CategoryIconMapper.iconFor(category.name)
    }

    Column(
        modifier = Modifier
            .width(96.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = category.name,
            modifier = Modifier.size(36.dp)
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = category.name,
            maxLines = 1,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
