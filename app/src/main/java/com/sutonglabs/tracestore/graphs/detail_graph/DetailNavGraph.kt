package com.sutonglabs.tracestore.graphs.detail_graph

import android.content.Context
import android.content.SharedPreferences
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.sutonglabs.tracestore.graphs.Graph
import com.sutonglabs.tracestore.ui.cart_screen.CartScreen
import com.sutonglabs.tracestore.ui.notification_screen.NotificationScreen
import com.sutonglabs.tracestore.ui.product_detail_screen.ProductDetailScreen
import com.sutonglabs.tracestore.viewmodels.ProductDetailViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController

@Composable
fun CartScreenWithContext(onItemClick: (Int, String, Context) -> Unit) {
    val context = LocalContext.current
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("your_prefs", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("TOKEN_KEY", "") ?: ""

    // Pass productId, token, and context together when navigating
    CartScreen(navController = NavController(context) , onItemClick = { productId ->
        onItemClick(productId, token, context)
    })
}

fun NavGraphBuilder.detailNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.DETAILS,
        startDestination = DetailScreen.ProductDetailScreen.route + "/{productId}/{token}"
    ) {
        composable(DetailScreen.CartScreen.route) {
            // Pass token and context when navigating
            CartScreenWithContext { productId, token, _ ->
                navController.navigate("productDetail/$productId/$token")
            }
        }
        composable(DetailScreen.NotificationScreen.route) {
            NotificationScreen()
        }
        composable(DetailScreen.ProductDetailScreen.route + "/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.toInt() ?: return@composable
            val token = backStackEntry.arguments?.getString("token") ?: ""

            // Get the ViewModel using Hilt
            val viewModel: ProductDetailViewModel = hiltViewModel()

            // Get the context
            val context = LocalContext.current

            // Pass parameters to ProductDetailScreen
            ProductDetailScreen(
                viewModel = viewModel,
                productId = productId,
                popBack = { navController.popBackStack() },
                context = context,
                token = token
            )
        }
    }
}


