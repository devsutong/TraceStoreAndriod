package com.sutonglabs.tracestore.ui.seller_dashboard_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sutonglabs.tracestore.models.SellerOrderItem
import com.sutonglabs.tracestore.models.SellerOrderResponse
import com.sutonglabs.tracestore.ui.common.ProductCardImage
import com.sutonglabs.tracestore.viewmodels.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerOrdersScreen(
    viewModel: OrderViewModel = hiltViewModel(),
    onProductClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val sellerOrders by viewModel.sellerOrders.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchSellerOrders(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Orders", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                errorMessage != null -> {
                    Text(
                        text = "Error: $errorMessage",
                        modifier = Modifier.padding(16.dp).align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                sellerOrders.isEmpty() -> {
                    Text("No orders found", modifier = Modifier.align(Alignment.Center))
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(sellerOrders) { order ->
                            SellerOrderCard(order = order, onProductClick = onProductClick, viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SellerOrderCard(
    order: SellerOrderResponse,
    onProductClick: (Int) -> Unit,
    viewModel: OrderViewModel
) {
    val context = LocalContext.current
    val status = order.status?.takeIf { it.isNotBlank() } ?: "Pending"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Order #${order.orderID}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                OrderStatusChip(status = status)
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Customer Information", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
            Text(text = order.buyer?.username ?: "Unknown Buyer", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            Text(text = order.buyer?.email ?: "No email", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Items", style = MaterialTheme.typography.labelLarge, color = Color.Gray)

            order.items?.forEach { item ->
                ProductItemView(item = item, onClick = { onProductClick(item.product.id) })
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            when (status) {
                "Success", "Pending" -> {
                    Button(
                        onClick = { viewModel.updateOrderStatus(context, order.orderID, "Shipped") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C))
                    ) {
                        Text("Mark as Shipped")
                    }
                }
                "Shipped" -> {
                    Button(
                        onClick = { viewModel.updateOrderStatus(context, order.orderID, "Delivered") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                    ) {
                        Text("Mark as Delivered")
                    }
                }
                "Delivered" -> {
                    OutlinedButton(
                        onClick = { },
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Order Delivered ✅", color = Color(0xFF388E3C))
                    }
                }
            }
        }
    }
}

@Composable
fun OrderStatusChip(status: String) {
    val backgroundColor = when (status) {
        "Pending" -> Color(0xFFFFF3E0)
        "Shipped" -> Color(0xFFE3F2FD)
        "Delivered" -> Color(0xFFE8F5E9)
        "Success" -> Color(0xFFE8F5E9)
        else -> Color(0xFFF5F5F5)
    }
    val textColor = when (status) {
        "Pending" -> Color(0xFFEF6C00)
        "Shipped" -> Color(0xFF1565C0)
        "Delivered" -> Color(0xFF2E7D32)
        "Success" -> Color(0xFF2E7D32)
        else -> Color.Black
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ProductItemView(
    item: SellerOrderItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProductCardImage(
            imageUrl = item.product.images.firstOrNull()?.imageUrl,
            name = item.product.name,
            height = 50.dp,
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.product.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                maxLines = 1
            )
            Text(
                text = "Qty: ${item.quantity} | ₹${item.product.price}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}
