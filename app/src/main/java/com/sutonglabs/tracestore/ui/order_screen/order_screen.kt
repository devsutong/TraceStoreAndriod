package com.sutonglabs.tracestore.ui.order_screen

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.sutonglabs.tracestore.viewmodels.OrderViewModel

@Composable
fun OrderScreen(
    orderViewModel: OrderViewModel = hiltViewModel()
) {
    val orders by orderViewModel.orders.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        orderViewModel.fetchOrders(context)
    }

    if (orders.isEmpty()) {
        Text("No orders available.")
    } else {
        orders.forEach { order ->
            Text("Order ID: ${order.id}, Total Price: $${order.totalPrice}, Status: ${order.status}")
            order.items.forEach { item ->
                Text("- ${item.productName}")
            }
        }
    }
}
