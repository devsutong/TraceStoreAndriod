package com.sutonglabs.tracestore.ui.cart_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sutonglabs.tracestore.models.CartProduct
import com.sutonglabs.tracestore.viewmodels.CartViewModel
import android.util.Log

@Composable
fun CartScreen(
    cartViewModel: CartViewModel = hiltViewModel(),
    onItemClick: (Int) -> Unit,
    navController: NavController
) {
    val cartState = cartViewModel.state.value

    val totalAmount = cartState.items.sumOf {
        it.product.price * it.quantity
    }.toDouble()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "My Cart",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        if (cartState.items.isEmpty()) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Your cart is empty.",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        navController.navigate("dashboard_screen") {
                            popUpTo("cart_screen") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Continue Shopping")
                }
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(cartState.items) { item ->

                    ProductCard(
                        product = item.product,
                        quantity = item.quantity,
                        onItemClick = { onItemClick(item.product.id) },
                        onQuantityChange = { newQuantity ->
                            cartViewModel.updateCartItem(item.id, newQuantity)
                        }
                    )
                }
            }

            Checkout(
                navController = navController,
                totalAmount = "₹${"%.2f".format(totalAmount)}",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ProductCard(
    product: CartProduct,
    quantity: Int,
    onItemClick: (Int) -> Unit,
    onQuantityChange: (Int) -> Unit,
) {

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onItemClick(product.id) },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = product.image,
                contentDescription = product.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "₹${product.price}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(onClick = {
                        if (quantity > 1) {
                            onQuantityChange(quantity - 1)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease"
                        )
                    }

                    Text(
                        text = "$quantity",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    IconButton(onClick = {
                        onQuantityChange(quantity + 1)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Checkout(
    navController: NavController,
    totalAmount: String,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Total: $totalAmount",
                style = MaterialTheme.typography.titleMedium
            )

            Button(
                onClick = {
                    navController.navigate("checkout_screen")
                }
            ) {
                Text("Checkout")
            }
        }
    }
}
