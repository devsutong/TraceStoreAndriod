package com.sutonglabs.tracestore.ui.dashboard_screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.sutonglabs.tracestore.common.Constants
import com.sutonglabs.tracestore.models.Product
import com.sutonglabs.tracestore.ui.common.shimmer
import com.sutonglabs.tracestore.viewmodels.DashboardViewModel

/**
 * The main dashboard screen of the application.
 * Displays a banner carousel, popular products, recommended products, and a grid of more products.
 *
 * @param productViewModel The ViewModel providing the product data.
 * @param onItemClick Callback for when a product is clicked.
 */
@Composable
fun DashboardScreen(
    productViewModel: DashboardViewModel = hiltViewModel(),
    onItemClick: (Int) -> Unit,
) {
    // State from the ViewModel containing product list, loading status, and errors.
    val state = productViewModel.state.value
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState) // Makes the whole screen scrollable
    ) {
        // 1. Top Banner Carousel for featured products.
        BannerCarousel(products = state.product ?: emptyList(), onItemClick = onItemClick)

        Spacer(modifier = Modifier.height(16.dp))

        // 2. "Popular Products" section with a horizontally scrolling list.
        SectionHeader(title = "Popular Products", onSeeAllClick = { /*TODO*/ })
        
        if (state.isLoading) {
            ShimmerRow() // Show loading shimmer
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                state.product?.let { products ->
                    items(products.shuffled()) { product ->
                        ProductCard(product, onItemClick)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. "Recommended for You" section.
        SectionHeader(title = "Recommended for You", onSeeAllClick = { /*TODO*/ })
        if (state.isLoading) {
            ShimmerRow()
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                state.product?.let { products ->
                    items(products.shuffled()) { product ->
                        ProductCard(product, onItemClick)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 4. "Explore More" section in a grid layout.
        SectionHeader(title = "Explore More", onSeeAllClick = { /*TODO*/ })
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            // Display products in a two-column grid.
            state.product?.chunked(2)?.forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { product ->
                        Box(modifier = Modifier.weight(1f)) {
                            GridProductCard(product, onItemClick)
                        }
                    }
                    // Add a spacer to fill the row if there's only one item.
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
        
        // Add padding at the bottom to avoid being obscured by the FloatingActionButton or BottomNavBar.
        Spacer(modifier = Modifier.height(80.dp))
    }
}

/**
 * A horizontally scrolling pager that displays featured product banners.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BannerCarousel(products: List<Product>, onItemClick: (Int) -> Unit) {
    if (products.isEmpty()) return
    
    // Take up to 5 products for the banner.
    val bannerProducts = products.take(5)
    val pagerState = rememberPagerState(pageCount = { bannerProducts.size })

    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 8.dp
        ) { page ->
            val product = bannerProducts[page]
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onItemClick(product.id) }
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box {
                    AsyncImage(
                        model = Constants.BASE_URL + product.images.firstOrNull()?.imageUrl,
                        contentDescription = product.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    // Add a gradient overlay to make the text more readable.
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                                    startY = 100f
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "New Arrival",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = product.name,
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
        
        // Indicator dots for the pager.
        Row(
            Modifier
                .height(20.dp)
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(bannerProducts.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(if (pagerState.currentPage == iteration) 12.dp else 8.dp)
                )
            }
        }
    }
}

/**
 * A header for a section, including a title and a "See All" button.
 */
@Composable
fun SectionHeader(title: String, onSeeAllClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        TextButton(onClick = onSeeAllClick) {
            Text(text = "See All", color = MaterialTheme.colorScheme.primary)
        }
    }
}

/**
 * A card to display a product in a horizontal list.
 */
@Composable
fun ProductCard(product: Product, onItemClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable { onItemClick(product.id) }
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Box {
                AsyncImage(
                    model = Constants.BASE_URL + product.images.firstOrNull()?.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentScale = ContentScale.Crop
                )
                // Wishlist icon (top right).
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(32.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.8f)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.FavoriteBorder,
                        contentDescription = "Add to Wishlist",
                        modifier = Modifier.padding(6.dp),
                        tint = Color.Gray
                    )
                }
            }
            
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "₹${product.price}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * A card to display a product in a grid.
 */
@Composable
fun GridProductCard(product: Product, onItemClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(product.id) }
            .shadow(elevation = 1.dp, shape = RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            AsyncImage(
                model = Constants.BASE_URL + product.images.firstOrNull()?.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
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
                    // Placeholder for a rating or badge.
                }
            }
        }
    }
}

/**
 * A row of shimmering placeholder cards for the loading state.
 */
@Composable
fun ShimmerRow() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(5) {
            Box(
                modifier = Modifier
                    .size(160.dp, 200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .shimmer() // Apply the shimmer modifier
            )
        }
    }
}
