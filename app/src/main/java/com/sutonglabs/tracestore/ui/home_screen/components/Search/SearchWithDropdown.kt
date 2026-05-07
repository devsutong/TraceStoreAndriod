package com.sutonglabs.tracestore.ui.home_screen.components.Search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.sutonglabs.tracestore.viewmodels.SearchViewModel
import kotlinx.coroutines.delay

@Composable
fun SearchWithDropdown(
    viewModel: SearchViewModel,
    onSearchSubmit: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var showDropdown by remember { mutableStateOf(false) }

    val suggestions = viewModel.suggestions
    
    // Fixed: Removed the frontend 'distinctBy' logic as requested. 
    // Now simply using the list provided by the backend, with basic null-safety.
    val categories = remember(suggestions.categories) { 
        suggestions.categories ?: emptyList()
    }
    val products = remember(suggestions.products) { 
        suggestions.products ?: emptyList()
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Column {
            SearchBar(
                value = query,
                onValueChange = {
                    query = it
                    showDropdown = it.isNotBlank() && it.length >= 2
                },
                onSearchSubmit = {
                    if (query.isNotBlank()) {
                        showDropdown = false
                        onSearchSubmit(query)
                        query = ""
                    }
                }
            )
        }

        if (showDropdown && (categories.isNotEmpty() || products.isNotEmpty())) {
            Popup(
                alignment = Alignment.TopStart,
                onDismissRequest = { showDropdown = false },
                properties = PopupProperties(focusable = false, dismissOnClickOutside = true)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .padding(top = 52.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp, topStart = 4.dp, topEnd = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 400.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        if (categories.isNotEmpty()) {
                            item {
                                SectionHeader(title = "Categories", icon = Icons.Outlined.Category)
                            }
                            items(categories) { cat ->
                                SuggestionRow(cat) {
                                    showDropdown = false
                                    onSearchSubmit(cat)
                                    query = ""
                                }
                            }
                        }

                        if (products.isNotEmpty()) {
                            if (categories.isNotEmpty()) {
                                item { 
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                                        thickness = 0.5.dp,
                                        color = MaterialTheme.colorScheme.outlineVariant
                                    ) 
                                }
                            }
                            item {
                                SectionHeader(title = "Products", icon = Icons.Outlined.Inventory2)
                            }
                            items(products) { prod ->
                                SuggestionRow(prod) {
                                    showDropdown = false
                                    onSearchSubmit(prod)
                                    query = ""
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(query) {
        if (query.length >= 2) {
            delay(300)
            viewModel.getSuggestions(query)
        } else {
            showDropdown = false
        }
    }
}

@Composable
private fun SectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun SuggestionRow(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
