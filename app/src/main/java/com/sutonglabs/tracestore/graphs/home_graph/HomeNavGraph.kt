package com.sutonglabs.tracestore.graphs.home_graph

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sutonglabs.tracestore.graphs.Graph
import com.sutonglabs.tracestore.graphs.cart_graph.cartNavGraph
import com.sutonglabs.tracestore.graphs.detail_graph.DetailScreen
import com.sutonglabs.tracestore.graphs.detail_graph.detailNavGraph
import com.sutonglabs.tracestore.repository.ProductRepository
import com.sutonglabs.tracestore.ui.add_product_screen.AddProductScreen
import com.sutonglabs.tracestore.ui.conversation_screen.ConversationScreen
import com.sutonglabs.tracestore.ui.dashboard_screen.DashboardScreen
import com.sutonglabs.tracestore.ui.favourite_screen.FavouriteScreen
import com.sutonglabs.tracestore.ui.order_screen.OrderScreen
import com.sutonglabs.tracestore.ui.product_admin.UpdateProductScreen
import com.sutonglabs.tracestore.ui.product_admin.ViewAllProductsScreen
import com.sutonglabs.tracestore.ui.profile_screen.ProfileScreen
import com.sutonglabs.tracestore.ui.qrscanner.QRScannerScreen
import com.sutonglabs.tracestore.ui.qrscanner.EnterUIDScreen
import com.sutonglabs.tracestore.ui.qrscanner.QRChoiceScreen
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
            DashboardScreen { productId ->
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
            ProfileScreen(
                navController = navHostController,
                onBackBtnClick = { navHostController.popBackStack() },
                onNavigateToAuth = onNavigateToAuth
            )
        }

        composable(ShopHomeScreen.OrderScreen.route) {
            OrderScreen()
        }

        composable("seller_dashboard_screen") {
            SellerDashboardScreen(navHostController)
        }

        composable("add_product_screen") {
            AddProductScreen(
                navHostController = navHostController,
                productRepository = productRepository
            )
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
            SellerOrdersScreen { productId ->
                navHostController.navigate(DetailScreen.ProductDetailScreen.route + "/$productId")
            }
        }

        // QR Flow
        composable("qr_choice") {
            QRChoiceScreen(navController = navHostController)
        }

        composable("enter_uid") {
            EnterUIDScreen(navController = navHostController)
        }

        composable("qrScanner") {
            QRScannerScreen(
                navController = navHostController,
                onResult = { scannedValue ->
                    Log.d("QR_RESULT", scannedValue)
                    // Example: navHostController.navigate("product_details_from_uid/$scannedValue")
                }
            )
        }
        composable("update_product_screen/{id}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            productId?.let {
                UpdateProductScreen(navHostController, productId = it)
            }
        }


        // Detail and Cart Graphs
        detailNavGraph(navController = navHostController)
        cartNavGraph(navController = navHostController)
    }
}
