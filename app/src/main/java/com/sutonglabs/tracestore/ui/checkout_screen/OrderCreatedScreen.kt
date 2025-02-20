package com.sutonglabs.tracestore.ui.checkout_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun OrderCreatedScreen(navController: NavController) {
    // Use a Box to center your content on the screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // A vertical Column to stack the elements
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // A creative check icon that signals success
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Order Success",
                tint = Color(0xFF4CAF50), // A nice green color
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            // A bold headline for the success message
            Text(
                text = "Order Placed Successfully!",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            // A brief description or thank you message
            Text(
                text = "Thank you for your purchase. Your order is on its way and will be processed shortly.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            // A button that navigates back to the home screen or any other screen
            Button(
                onClick = {
                    // Replace "home" with your target route
                    navController.navigate("dashboard_screen") {
                        // Remove this screen from the backstack to prevent returning here
                        popUpTo("order_created_screen") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continue Shopping")
            }
        }
    }
}