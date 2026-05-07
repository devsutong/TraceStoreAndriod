package com.sutonglabs.tracestore.ui.home_screen.components.review

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ReviewInputSection(
    onSubmit: (Int, String) -> Unit
) {
    var rating by remember { mutableStateOf(0) }
    var reviewText by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text(
            text = "Add Your Review",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ⭐ Star Rating
        Row {
            for (i in 1..5) {
                Icon(
                    imageVector = if (i <= rating) Icons.Filled.Star else Icons.Filled.StarBorder,
                    contentDescription = "Star",
                    tint = Color(0xFFFFC107),
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { rating = i }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ✍️ Review Input
        OutlinedTextField(
            value = reviewText,
            onValueChange = {
                reviewText = it
                error = ""
            },
            placeholder = { Text("Write your review...") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 4,
            isError = error.isNotEmpty()
        )

        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🚀 Submit Button
        Button(
            onClick = {
                when {
                    rating == 0 -> error = "Please select a rating"
                    reviewText.isBlank() -> error = "Review cannot be empty"
                    else -> {
                        onSubmit(rating, reviewText)
                        reviewText = ""
                        rating = 0
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit Review")
        }
    }
}
