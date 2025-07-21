package com.sutonglabs.tracestore.ui.product_admin

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sutonglabs.tracestore.models.ProductCreate
import com.sutonglabs.tracestore.viewmodels.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProductScreen(
    navController: NavController,
    productId: Int,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val product = uiState.products.find { it.id == productId }

    // Only show form if product is loaded
    if (product == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Product not found")
        }
        return
    }

    var name by remember { mutableStateOf(product.name) }
    var description by remember { mutableStateOf(product.description) }
    var price by remember { mutableStateOf(product.price.toString()) }
    var priceUnit by remember { mutableStateOf(product.priceUnit) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Update Product") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = priceUnit,
                onValueChange = { priceUnit = it },
                label = { Text("Price Unit") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(onClick = {
                if (name.isNotBlank() && description.isNotBlank() && price.isNotBlank()) {
                    val updatedProduct = ProductCreate(
                        name = name,
                        description = description,
                        image = product.image, // Retain old image
                        price = price.toIntOrNull() ?: 0,
                        priceUnit = priceUnit,
                        categoryIds = product.categoryIds
                    )
                    viewModel.updateProduct(productId, updatedProduct, context)
                    Toast.makeText(context, "Product Updated", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Update Product")
            }
        }
    }
}
