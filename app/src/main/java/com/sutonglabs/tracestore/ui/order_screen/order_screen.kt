package com.sutonglabs.tracestore.ui.order_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.sutonglabs.tracestore.api.response_model.Order
import com.sutonglabs.tracestore.api.response_model.OrderItem
import com.sutonglabs.tracestore.common.Constants
import com.sutonglabs.tracestore.ui.common.ProductCardImage
import com.sutonglabs.tracestore.viewmodels.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    orderViewModel: OrderViewModel = hiltViewModel()
) {
    val orders by orderViewModel.order.collectAsState()
    val errorMessage by orderViewModel.errorMessage.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        orderViewModel.fetchOrders(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My Orders") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                errorMessage != null -> {
                    Text("Error: $errorMessage", color = MaterialTheme.colorScheme.error)
                }
                orders.isEmpty() -> {
                    Text("You have no orders.")
                }
                else -> {
                    LazyColumn {
                        items(orders) { order ->
                            OrderCard(order)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: Order) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Total Price: ₹${order.totalAmount}")
            Text(text = "Status: ${order.status}", color = Color.Blue)
            Text(text = "Date: ${order.createdAt}")

            Spacer(modifier = Modifier.height(8.dp))
            Text("Items:", fontWeight = FontWeight.Bold)

            order.OrderItems.forEach { item ->
                OrderItemRow(item)
            }
        }
    }
}

@Composable
fun OrderItemRow(item: OrderItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //todo: make it reusable
            AsyncImage(
                model = item.Product.image,
                contentDescription = item.Product.name,
                modifier = Modifier
                    .size(80.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = item.Product.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Qty: ${item.quantity}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
