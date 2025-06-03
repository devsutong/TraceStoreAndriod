package com.sutonglabs.tracestore.ui.seller

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.sutonglabs.tracestore.common.Constants
import com.sutonglabs.tracestore.models.SellerOrderItem
import com.sutonglabs.tracestore.models.SellerOrderResponse
import com.sutonglabs.tracestore.viewmodels.OrderViewModel

@Composable
fun SellerOrdersScreen(
    viewModel: OrderViewModel = hiltViewModel(),
    onProductClick: (Int) -> Unit
) {
    val context = LocalContext.current
    val sellerOrders = viewModel.sellerOrders.collectAsState().value
    val errorMessage = viewModel.errorMessage.collectAsState().value

    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        viewModel.fetchSellerOrders(context)
        isLoading = false
    }

    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        errorMessage != null -> {
            Text(
                text = "Error: $errorMessage",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.error
            )
        }
        sellerOrders.isEmpty() -> {
            Text("No orders found", modifier = Modifier.padding(16.dp))
        }
        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                items(sellerOrders) { order ->
                    SellerOrderCard(order = order, onProductClick = onProductClick)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun SellerOrderCard(
    order: SellerOrderResponse,
    onProductClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val buyerName = order.buyer?.username ?: "Unknown Buyer"
            val buyerEmail = order.buyer?.email ?: "No email"

            Text(text = "Order ID: ${order.orderID}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Buyer: $buyerName")
            Text(text = "Email: $buyerEmail")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Items:", style = MaterialTheme.typography.titleSmall)

            order.items?.forEach { item ->
                ProductItemView(item = item, onClick = { onProductClick(item.product.id) })
            }
        }
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
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberImagePainter(Constants.BASE_URL + item.product.image),
            contentDescription = item.product.name,
            modifier = Modifier
                .size(60.dp)
                .padding(end = 12.dp),
            contentScale = ContentScale.Crop
        )

        Column {
            Text(
                text = item.product.name,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium)
            )
            Text(
                text = "Quantity: ${item.quantity}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Price: ₹${item.product.price} ${item.product.priceUnit}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


