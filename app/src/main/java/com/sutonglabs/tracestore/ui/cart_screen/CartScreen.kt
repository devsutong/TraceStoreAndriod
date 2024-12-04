package com.sutonglabs.tracestore.ui.cart_screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.sutonglabs.tracestore.common.Constants
import com.sutonglabs.tracestore.models.CartProduct
import com.sutonglabs.tracestore.viewmodels.CartViewModel

@Composable
fun CartScreen(
    cartViewModel: CartViewModel = hiltViewModel(),
    onItemClick: (Int) -> Unit
) {
    val state = cartViewModel.state.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "My Cart",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // LazyColumn for scrollable content
        LazyColumn(
            modifier = Modifier
//                .weight(1f) // Ensures LazyColumn takes up remaining space
                .fillMaxWidth()
                .height(680.dp),
            state = rememberLazyListState()
        ) {
            state.product?.let { products ->
                items(products) { product ->
                    ProductCard(product.product, product.quantity, onItemClick)
                }
            }
        }

        // Checkout Bar (Fixed at the bottom)
        Checkout(
            totalAmount = "1234.56",
            onCheckoutClick = { /* Handle checkout click */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}

@Composable
fun ProductCard(product: CartProduct, quantity: Int,  onItemClick: (Int) -> Unit) {
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
            Image(
                painter = rememberImagePainter(Constants.BASE_URL + product.image),
                contentDescription = null,
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
                Text(
                    text = "Qty $quantity",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "₹${product.price}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun Checkout(totalAmount: String, onCheckoutClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
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
            Button(onClick = onCheckoutClick) {
                Text(text = "Checkout")
            }
        }
    }
}




//@Composable
//fun CartScreen(
//    cartViewModel: CartViewModel = hiltViewModel(),
//    onItemClick: (Int) -> Unit
//) {
//    val state = cartViewModel.state.value
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//        ) {
//            LazyColumn(
//                modifier = Modifier.weight(1f), // Constrain height
//                state = rememberLazyListState()
//            ) {
//                item {
//                    Text(
//                        text = "My Cart",
//                        style = MaterialTheme.typography.headlineSmall,
//                        modifier = Modifier.padding(vertical = 8.dp)
//                    )
//                }
//                state.product?.let { products ->
//                    items(products) { product ->
//                        ProductCard(product.product, onItemClick)
//                        Log.d("CartScreen", "Products: ${product.product}")
//
//                    }
//                }
//            }
//        }
//
//        Checkout(
//            totalAmount = "1234.56",
//            onCheckoutClick = { /* Handle checkout click */ },
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .fillMaxWidth()
//        )
//    }
//}
//
//@Composable
//fun ProductCard(product: CartProduct, onItemClick: (Int) -> Unit) {
//    Card(
//        modifier = Modifier
//            .padding(8.dp)
//            .clickable { onItemClick(product.id) }
//            .fillMaxWidth()
//            .aspectRatio(3f),
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//    ) {
//        Row(modifier = Modifier.padding(8.dp)) {
//            // Image on the left
//            Image(
//                painter = rememberImagePainter(Constants.BASE_URL + product.image),
//                contentDescription = null,
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .aspectRatio(1f),
//                contentScale = ContentScale.Crop
//            )
//
//            Spacer(modifier = Modifier.width(8.dp))
//
//
//            Column(
//                verticalArrangement = Arrangement.SpaceBetween,
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .padding(vertical = 4.dp)
//            ) {
//                Text(
//                    text = product.name,
//                    style = MaterialTheme.typography.titleMedium,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//                Spacer(modifier = Modifier.weight(1f))
//                Text(
//                    text = "${product.price}",
//                    style = MaterialTheme.typography.bodyMedium
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun Checkout(totalAmount: String, onCheckoutClick: () -> Unit, modifier: Modifier = Modifier) {
//    Row(
//        modifier = modifier
//            .fillMaxWidth()
//            .height(72.dp)
//            .padding(8.dp),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Text(
//            text = "Total: $totalAmount",
//            style = MaterialTheme.typography.titleMedium,
//            modifier = Modifier.padding(start = 16.dp)
//        )
//        Button(
//            onClick = onCheckoutClick,
//            modifier = Modifier.padding(end = 16.dp)
//        ) {
//            Text(text = "Proceed to Checkout")
//        }
//    }
//}