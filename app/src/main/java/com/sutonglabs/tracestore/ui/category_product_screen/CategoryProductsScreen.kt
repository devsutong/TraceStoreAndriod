package com.sutonglabs.tracestore.ui.category_product_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sutonglabs.tracestore.graphs.detail_graph.DetailScreen
import com.sutonglabs.tracestore.viewmodels.CategoryProductsViewModel
import com.sutonglabs.tracestore.ui.dashboard_screen.ProductCard
import androidx.navigation.NavController

@Composable
fun CategoryProductsScreen(
    navController: NavController,
    categoryId: Int,
    viewModel: CategoryProductsViewModel = hiltViewModel()
) {
    LaunchedEffect(categoryId) {
        viewModel.load(categoryId)
    }

    when {
        viewModel.loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        viewModel.products.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No products found")
            }
        }

        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp)
            ) {
                items(viewModel.products) { product ->
                    ProductCard(
                        product = product,
                        onItemClick = {
                            navController.navigate(
                                DetailScreen.ProductDetailScreen.route + "/${product.id}"
                            )
                        }
                    )
                }
            }
        }
    }
}
