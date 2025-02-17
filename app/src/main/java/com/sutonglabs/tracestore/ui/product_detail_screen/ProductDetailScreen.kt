package com.sutonglabs.tracestore.ui.product_detail_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.sutonglabs.tracestore.viewmodels.ProductDetailViewModel
import com.sutonglabs.tracestore.common.Constants
import android.content.Context
import android.util.Log

@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel,
    productId: Int,
    popBack: () -> Unit,
    context: Context,
    token: String
) {
    val state = viewModel.state.value

    // Trigger API call with the product ID
    LaunchedEffect(productId) {
        // Ensure the correct token and context are passed here
        viewModel.getProductDetail(productId, context)
    }

    // Log the current state
    Log.d("ProductDetailScreen", "Current state: $state")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (state.errorMessage.isNotEmpty()) {
            Text(
                text = state.errorMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        } else {
            state.productDetail?.let { product ->

                Image(
                    painter = rememberImagePainter(Constants.BASE_URL + product.image),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(bottom = 16.dp),
                    contentScale = ContentScale.Crop
                )
                Text(text = product.name, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = product.description, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "${product.price} INR", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        viewModel.addToCart(product.id, context)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(CornerSize(8.dp))
                ) {
                    Text("Add to Cart")
                }

            } ?: run {
                Text(
                    text = "Product not found.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
