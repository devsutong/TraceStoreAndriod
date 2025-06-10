package com.sutonglabs.tracestore.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Admin Dashboard") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                AdminWelcomeCard()
            }

            // First Row
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        AdminFeatureCard(
                            title = "User Management",
                            description = "Manage all registered users and their roles.",
                            icon = Icons.Default.Group,
                            buttonText = "Manage Users",
                            onClick = { /* navigate to user management */ }
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        AdminFeatureCard(
                            title = "Product Management",
                            description = "Manage products, inventory, and stock.",
                            icon = Icons.Default.Inventory,
                            buttonText = "Manage Products",
                            onClick = { /* navigate to product management */ }
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        AdminFeatureCard(
                            title = "Order Management",
                            description = "View and manage customer orders.",
                            icon = Icons.Default.ShoppingCart,
                            buttonText = "View Orders",
                            onClick = { /* navigate to order management */ }
                        )
                    }
                }
            }

            // Second Row
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        AdminFeatureCard(
                            title = "Category Management",
                            description = "Organize and manage product categories.",
                            icon = Icons.Default.Category,
                            buttonText = "Manage Categories",
                            onClick = { /* navigate to category management */ }
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        AdminFeatureCard(
                            title = "Payment History",
                            description = "Track and manage all payment records.",
                            icon = Icons.Default.Receipt,
                            buttonText = "View Payments",
                            onClick = { /* navigate to payment history */ }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AdminWelcomeCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Welcome, Admin!", style = MaterialTheme.typography.headlineSmall)
            Text(
                text = "Manage your admin tasks from the dashboard below.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun AdminFeatureCard(
    title: String,
    description: String,
    icon: ImageVector,
    buttonText: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = title, modifier = Modifier.size(48.dp))
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(description, style = MaterialTheme.typography.bodySmall)
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(buttonText)
            }
        }
    }
}


