package com.sutonglabs.tracestore.ui.dashboard_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.sutonglabs.tracestore.models.Product
import com.sutonglabs.tracestore.viewmodels.DashboardViewModel
import com.sutonglabs.tracestore.common.Constants

@Composable
fun DashboardScreen(
    popularProductState: LazyListState = rememberLazyListState(),
    recommendedProductState: LazyListState = rememberLazyListState(),
    productViewModel: DashboardViewModel = hiltViewModel(),
    onItemClick: (Int) -> Unit,
) {
    val state = productViewModel.state.value
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp) // slightly reduced overall padding
            .verticalScroll(scrollState)
            .heightIn(max = 2048.dp)
    ) {
        Text(
            text = "Popular Products",
            style = MaterialTheme.typography.titleMedium, // using a slightly smaller text style
            modifier = Modifier.padding(vertical = 4.dp)
        )

        LazyRow(state = popularProductState) {
            state.product?.let { products ->
                items(products.shuffled()) { product ->
                    ProductCard(product, onItemClick)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Recommended for You",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        LazyRow(state = recommendedProductState) {
            state.product?.let { products ->
                items(products.shuffled()) { product ->
                    ProductCard(product, onItemClick)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "More Products",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        LazyColumn(state = rememberLazyListState()) {
            state.product?.let { products ->
                items(products.chunked(3)) { productPair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        productPair.forEach { product ->
                            MoreProductCard(product, onItemClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, onItemClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .clickable { onItemClick(product.id) }
            .width(130.dp) // scaled down width
            .height(160.dp), // scaled down height
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Image(
                painter = rememberImagePainter(Constants.BASE_URL + product.image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp), // scaled down image height
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = product.name,
                // Optionally scale down font size by copying and modifying the style
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = MaterialTheme.typography.bodyMedium.fontSize * 0.9f),
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Text(
                text = "${product.price} INR",
                style = MaterialTheme.typography.bodySmall, // using a smaller body style
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

@Composable
fun MoreProductCard(product: Product, onItemClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .clickable { onItemClick(product.id) }
            .width(125.dp) // scaled down width
            .height(155.dp), // scaled down height
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Image(
                painter = rememberImagePainter(Constants.BASE_URL + product.image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp), // scaled down image height
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = product.name,
                // Optionally scale down font size by copying and modifying the style
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = MaterialTheme.typography.bodyMedium.fontSize * 0.9f),
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Text(
                text = "${product.price} INR",
                style = MaterialTheme.typography.bodySmall, // using a smaller body style
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}
