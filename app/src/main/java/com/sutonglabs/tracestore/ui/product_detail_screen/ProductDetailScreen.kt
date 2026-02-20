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
import com.sutonglabs.tracestore.ui.common.ProductImageCarousel

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

                ProductImageCarousel(images = product.images)

                val isVerified = product.productBlockchainStatus?.blockchainStatus == true

                Text(
                    text = if (isVerified) "Blockchain: Verified ✅" else "Blockchain: Not Synced ❌",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isVerified) androidx.compose.ui.graphics.Color(0xFF4CAF50) else androidx.compose.ui.graphics.Color.Red
                )
                Log.d("ProductDetailScreen", "Current state: $state")


                Text(text = product.name, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = product.description, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "${product.price} INR", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))

                // Only show this if the product isn't on the ledger yet
                //TODO: GLITCH WHEN I CLICK ON BLOCKCHAIN STATUS BUTTON, IMAGE DISSAPEARS AND IT DOESNOT UPDATES
                if (!isVerified) {
                    Button(
                        onClick = {
                            // This function needs to be added to your ProductDetailViewModel
                            viewModel.syncProductToBlockchain(product.id, context)
                        },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        shape = RoundedCornerShape(CornerSize(8.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("Sync to Blockchain")
                    }
                }

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
