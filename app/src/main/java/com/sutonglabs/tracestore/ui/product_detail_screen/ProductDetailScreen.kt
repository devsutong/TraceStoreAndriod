package com.sutonglabs.tracestore.ui.product_detail_screen

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sutonglabs.tracestore.models.Product
import com.sutonglabs.tracestore.viewmodels.ProductDetailViewModel
import com.sutonglabs.tracestore.ui.common.ProductImageCarousel

/**
 * The main screen for displaying the details of a single product.
 * It includes an image carousel, product information, blockchain status, and an add-to-cart bar.
 *
 * @param viewModel The ViewModel that provides the product details.
 * @param productId The ID of the product to display.
 * @param popBack Callback to navigate back to the previous screen.
 * @param context The Android context.
 * @param token The authorization token.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel,
    productId: Int,
    popBack: () -> Unit,
    context: Context,
    token: String
) {
    // Observe the state from the ViewModel.
    val state = viewModel.state.value
    val scrollState = rememberScrollState()

    // Fetch the product details when the screen is first composed.
    LaunchedEffect(productId) {
        viewModel.getProductDetail(productId, context)
    }

    Scaffold(
        // The bottom bar is only shown when the product details are successfully loaded.
        bottomBar = {
            if (state.productDetail != null) {
                DetailBottomBar(
                    price = state.productDetail.price.toString(),
                    onAddToCart = { viewModel.addToCart(state.productDetail.id, context) }
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                // Show a loading indicator while fetching data.
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                // Show an error message if something went wrong.
                state.errorMessage.isNotEmpty() -> {
                    ErrorView(state.errorMessage)
                }
                // Show the product details when loaded.
                state.productDetail != null -> {
                    state.productDetail?.let { product ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                        ) {
                            // Header containing the product image carousel and navigation buttons.
                            ProductImageHeader(product, popBack)

                            Column(modifier = Modifier.padding(16.dp)) {
                                // Section with product name, price, and other info.
                                ProductInfoSection(product)

                                Spacer(modifier = Modifier.height(24.dp))

                                // Card displaying the blockchain verification status.
                                BlockchainStatusCard(
                                    isVerified = product.productBlockchainStatus?.blockchainStatus == true,
                                    onSyncClick = { viewModel.syncProductToBlockchain(product.id, context) }
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                // Product description section.
                                Text(
                                    text = "Description",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = product.description,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 24.sp
                                )

                                Spacer(modifier = Modifier.height(32.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Header section containing the product image carousel and top action buttons.
 */
@Composable
fun ProductImageHeader(product: Product, onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().height(400.dp)) {
        ProductImageCarousel(images = product.images)

        // Top controls (Back, Share, Favorite)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .statusBarsPadding(), // Ensures content is not drawn under the status bar.
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.background(Color.White.copy(alpha = 0.8f), CircleShape)
            ) {
                Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
            }

            Row {
                IconButton(
                    onClick = { /* TODO: Implement Share functionality */ },
                    modifier = Modifier.background(Color.White.copy(alpha = 0.8f), CircleShape)
                ) {
                    Icon(Icons.Rounded.Share, contentDescription = "Share")
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = { /* TODO: Implement Favorite functionality */ },
                    modifier = Modifier.background(Color.White.copy(alpha = 0.8f), CircleShape)
                ) {
                    Icon(Icons.Rounded.FavoriteBorder, contentDescription = "Favorite")
                }
            }
        }
    }
}

/**
 * Section displaying the product's name, price, and shipping information.
 */
@Composable
fun ProductInfoSection(product: Product) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "High Quality Traceable Product",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "₹${product.price}",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(12.dp))
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "FREE SHIPPING",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

/**
 * A card that prominently displays the product's blockchain verification status.
 */
@Composable
fun BlockchainStatusCard(isVerified: Boolean, onSyncClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isVerified) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isVerified) Icons.Default.Verified else Icons.Default.Warning,
                contentDescription = if (isVerified) "Verified" else "Not Verified",
                tint = if (isVerified) Color(0xFF2E7D32) else Color(0xFFE65100),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isVerified) "Verified on Blockchain" else "Not Synced with Blockchain",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isVerified) Color(0xFF1B5E20) else Color(0xFFBF360C)
                )
                Text(
                    text = if (isVerified) "This product\'s authenticity is guaranteed." else "Sync this product to secure its record.",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isVerified) Color(0xFF2E7D32) else Color(0xFFE65100)
                )
            }
            // Show sync button only if the product is not yet verified.
            if (!isVerified) {
                TextButton(onClick = onSyncClick) {
                    Text("SYNC NOW", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * The bottom action bar for the detail screen, containing the price and Add to Cart button.
 */
@Composable
fun DetailBottomBar(price: String, onAddToCart: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Total Price", style = MaterialTheme.typography.labelMedium)
                Text(
                    text = "₹$price",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Button(
                onClick = onAddToCart,
                modifier = Modifier
                    .weight(1.5f)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = "Add to cart icon")
                Spacer(modifier = Modifier.width(8.dp))
                Text("ADD TO CART", fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * A composable to display an error message in the center of the screen.
 */
@Composable
fun ErrorView(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Error, contentDescription = "Error", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(48.dp))
            Text(text = message, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
        }
    }
}
