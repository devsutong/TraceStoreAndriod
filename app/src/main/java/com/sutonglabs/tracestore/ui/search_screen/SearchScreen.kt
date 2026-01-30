package com.sutonglabs.tracestore.ui.search_screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sutonglabs.tracestore.ui.dashboard_screen.ProductCard
import com.sutonglabs.tracestore.viewmodels.SearchViewModel
import com.sutonglabs.tracestore.graphs.detail_graph.DetailScreen.ProductDetailScreen
import com.sutonglabs.tracestore.graphs.detail_graph.DetailScreen


@Composable
fun SearchScreen(
    initialQuery: String,
    navController: NavHostController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val products = viewModel.products

    LaunchedEffect(initialQuery) {
        if (initialQuery.isNotBlank()) {
            viewModel.search(initialQuery)
        }
    }

    LazyColumn {
        items(products) { product ->
            ProductCard(
                product = product,
                onItemClick = { productId ->
                    navController.navigate(
                        DetailScreen.ProductDetailScreen.route + "/$productId"
                    )
                }
            )
        }

        // simple pagination (keep as-is)
        item {
            LaunchedEffect(products.size) {
                if (products.isNotEmpty()) {
                    viewModel.loadMore(initialQuery)
                }
            }
        }
    }
}
