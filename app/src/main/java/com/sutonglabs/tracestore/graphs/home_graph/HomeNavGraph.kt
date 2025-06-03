package com.sutonglabs.tracestore.graphs.home_graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
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
import com.sutonglabs.tracestore.ui.order_screen.OrderScreen // Import your OrdersScreen
import com.sutonglabs.tracestore.repository.ProductRepository
import com.sutonglabs.tracestore.ui.seller.SellerOrdersScreen
import com.sutonglabs.tracestore.ui.seller_dashboard_screen.SellerDashboardScreen
import com.sutonglabs.tracestore.ui.seller_dashboard_screen.SellerProductListScreen
import com.sutonglabs.tracestore.ui.update_profile_screen.UpdateProfileScreen
import com.sutonglabs.tracestore.viewmodels.UserViewModel

@Composable
fun HomeNavGraph(
    navHostController: NavHostController,
    productRepository: ProductRepository,
    userViewModel: UserViewModel,
    onNavigateToAuth: () -> Unit,
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
            ProfileScreen(navController = navHostController,
                onBackBtnClick = { navHostController.popBackStack() },
                onNavigateToAuth = onNavigateToAuth)
        }
        composable("seller_dashboard_screen") {
            SellerDashboardScreen(navHostController)
        }

        composable(ShopHomeScreen.OrderScreen.route) {
            OrderScreen()
        }


        composable("add_product_screen") {
            AddProductScreen(navHostController = navHostController, productRepository = productRepository)
        }

        composable("update_profile_screen") {
            UpdateProfileScreen(navController = navHostController)
        }
        composable("seller_product_list") {
            SellerProductListScreen(
                navController = navHostController,
                userViewModel = userViewModel
            ) { productId ->
                navHostController.navigate(DetailScreen.ProductDetailScreen.route + "/$productId")
            }
        }


        composable("seller_orders_screen") {
            SellerOrdersScreen(onProductClick = { productId ->
                navHostController.navigate(DetailScreen.ProductDetailScreen.route + "/$productId")
            })
        }


        // detail and cart graphs
        detailNavGraph(navController = navHostController)
        cartNavGraph(navController = navHostController)


    }
}
