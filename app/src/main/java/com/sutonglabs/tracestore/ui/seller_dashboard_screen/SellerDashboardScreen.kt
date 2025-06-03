package com.sutonglabs.tracestore.ui.seller_dashboard_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.ui.graphics.Color

@Composable
fun SellerDashboardScreen(navController: NavController) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Stylized Title
            Text(
                text = "Seller Dashboard",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color(0xFF2E7D32), // Dark green
                    fontSize = 28.sp
                ),
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                thickness = 5.dp,
                color = Color(0xFFB2DFDB)
            )

            // Rest of your dashboard
            DashboardCard(
                title = "Add Product",
                icon = Icons.Default.AddBox,
                onClick = { navController.navigate("add_product_screen") }
            )
            DashboardCard(
                title = "View Products",
                icon = Icons.AutoMirrored.Filled.List,
                onClick = { navController.navigate("seller_product_list") }
            )


            DashboardCard(
                title = "View Orders",
                icon = Icons.AutoMirrored.Filled.ReceiptLong,
                onClick = { navController.navigate("seller_orders_screen")
                }
            )

            DashboardCard(
                title = "Payment History",
                icon = Icons.Default.Payments,
                onClick = { /* TODO */ }
            )
        }
    }
}

@Composable
fun DashboardCard(title: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F5E9) // Light green background
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(32.dp),
                tint = Color(0xFF388E3C) // Green icon
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = title,
                fontSize = 20.sp,
                color = Color(0xFF2E7D32), // Darker green text
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

