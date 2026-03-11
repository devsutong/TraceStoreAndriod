package com.sutonglabs.tracestore.ui.cart_screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sutonglabs.tracestore.models.CartItem
import com.sutonglabs.tracestore.models.CartProduct
import com.sutonglabs.tracestore.ui.common.ProductCardImage
import com.sutonglabs.tracestore.viewmodels.CartViewModel

/**
 * The main screen for displaying the user's shopping cart.
 * It shows a list of items, an empty cart view if applicable, and a summary bar at the bottom.
 *
 * @param cartViewModel The ViewModel that holds the cart's state and logic.
 * @param onItemClick Callback function to handle clicks on individual cart items.
 * @param navController The NavController for handling navigation actions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartViewModel: CartViewModel = hiltViewModel(),
    onItemClick: (Int) -> Unit,
    navController: NavController
) {
    val cartState = cartViewModel.state.value
    val totalAmount = cartState.items.sumOf { it.product.price * it.quantity }
    val formattedTotal = "₹%.2f".format(totalAmount.toDouble())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Shopping Cart", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            // The bottom summary bar is only shown if the cart is not empty.
            if (cartState.items.isNotEmpty()) {
                CartBottomSummary(formattedTotal) {
                    navController.navigate("checkout_screen")
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (cartState.items.isEmpty() && !cartState.isLoading) {
                // Show a dedicated view when the cart is empty.
                EmptyCartView { navController.navigate("dashboard_screen") }
            } else {
                // Display the list of cart items.
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(cartState.items, key = { it.id }) { item ->
                        CartItemCard(
                            item = item,
                            onItemClick = { onItemClick(item.product.id) },
                            onQuantityChange = { newQty ->
                                cartViewModel.updateCartItem(item.id, newQty)
                            }
                        )
                    }
                    // Add spacer at the bottom to avoid the last item being obscured by the bottom bar.
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }

            // Show a loading indicator in the center of the screen when data is being fetched.
            if (cartState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

/**
 * A card that represents a single item in the shopping cart.
 *
 * @param item The cart item to display.
 * @param onItemClick Callback for when the card is clicked.
 * @param onQuantityChange Callback for when the quantity is changed.
 */
@Composable
fun CartItemCard(
    item: CartItem,
    onItemClick: () -> Unit,
    onQuantityChange: (Int) -> Unit
) {
    val product = item.product

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clickable(onClick = onItemClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Image
            Surface(
                modifier = Modifier.size(100.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                ProductCardImage(
                    imageUrl = product.image,
                    name = product.name,
                    height = 100.dp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Product Details (Name, Price, Quantity Control)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "High Quality",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹${product.price}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    // Quantity +/- controls
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
                                RoundedCornerShape(8.dp)
                            )
                    ) {
                        IconButton(
                            onClick = { if (item.quantity > 1) onQuantityChange(item.quantity - 1) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease Quantity", modifier = Modifier.size(16.dp))
                        }
                        Text(
                            text = "${item.quantity}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        IconButton(
                            onClick = { onQuantityChange(item.quantity + 1) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase Quantity", modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

/**
 * A view displayed when the shopping cart is empty.
 *
 * @param onShopNow Callback for the "Start Shopping" button.
 */
@Composable
fun EmptyCartView(onShopNow: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Your cart is empty",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Looks like you haven\'t added anything to your cart yet.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onShopNow,
            modifier = Modifier.height(56.dp).padding(horizontal = 32.dp),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("START SHOPPING", fontWeight = FontWeight.Bold)
        }
    }
}

/**
 * A bottom bar that displays the total price and a checkout button.
 *
 * @param total The formatted total price string.
 * @param onCheckout Callback for the checkout button.
 */
@Composable
fun CartBottomSummary(total: String, onCheckout: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 16.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Grand Total", style = MaterialTheme.typography.labelMedium)
                    Text(
                        text = total,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Button(
                    onClick = onCheckout,
                    modifier = Modifier.height(56.dp).width(180.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("CHECKOUT", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null)
                }
            }
        }
    }
}
