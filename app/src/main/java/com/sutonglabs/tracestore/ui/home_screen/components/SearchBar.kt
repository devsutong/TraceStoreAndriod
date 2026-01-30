package com.sutonglabs.tracestore.ui.home_screen.components

import androidx.appcompat.widget.SearchView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSearchSubmit: () -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        placeholder = { Text("Search product") },
        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = "search")
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearchSubmit() }
        ),
        modifier = Modifier.fillMaxWidth()
    )
}
