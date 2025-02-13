package com.sutonglabs.tracestore.graphs.home_graph

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sutonglabs.tracestore.graphs.Graph
import com.sutonglabs.tracestore.graphs.cart_graph.cartNavGraph
import com.sutonglabs.tracestore.graphs.detail_graph.DetailScreen
import com.sutonglabs.tracestore.graphs.detail_graph.detailNavGraph
import com.sutonglabs.tracestore.ui.add_product_screen.AddProductScreen
import com.sutonglabs.tracestore.ui.conversation_screen.ConversationScreen
import com.sutonglabs.tracestore.ui.dashboard_screen.DashboardScreen
import com.sutonglabs.tracestore.ui.favourite_screen.FavouriteScreen
import com.sutonglabs.tracestore.ui.profile_screen.ProfileScreen
import com.sutonglabs.tracestore.repository.ProductRepository
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.scopes.ActivityScoped

@Composable
fun HomeNavGraph(
    navHostController: NavHostController,
    productRepository: ProductRepository // Injected here
) {
    NavHost(
        navController = navHostController,
        route = Graph.HOME,
        startDestination = ShopHomeScreen.DashboardScreen.route
    ) {
        composable(ShopHomeScreen.DashboardScreen.route) {
            DashboardScreen() { productId ->
                navHostController.navigate(DetailScreen.ProductDetailScreen.route + "/$productId")
            }
        }
        composable(ShopHomeScreen.FavouriteScreen.route) {
            FavouriteScreen()
        }
        composable(ShopHomeScreen.ConversationScreen.route) {
            ConversationScreen()
        }
        composable(ShopHomeScreen.ProfileScreen.route) {
            ProfileScreen {
                navHostController.popBackStack()
            }
        }

        // Pass navHostController and productRepository to AddProductScreen
        composable("add_product_screen") {
            AddProductScreen(navHostController = navHostController, productRepository = productRepository)
        }

        // detail graph
        detailNavGraph(navController = navHostController)
        cartNavGraph(navController = navHostController)
    }
}
