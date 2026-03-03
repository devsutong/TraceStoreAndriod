package com.sutonglabs.tracestore.ui.home_screen

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sutonglabs.tracestore.graphs.detail_graph.DetailScreen
import com.sutonglabs.tracestore.graphs.home_graph.HomeNavGraph
import com.sutonglabs.tracestore.graphs.home_graph.ShopHomeScreen
import com.sutonglabs.tracestore.repository.ProductRepository
import com.sutonglabs.tracestore.ui.home_screen.components.BottomNavBar
import com.sutonglabs.tracestore.ui.home_screen.components.FloatingAddProductButton
import com.sutonglabs.tracestore.viewmodels.CategoryViewModel
import com.sutonglabs.tracestore.viewmodels.UserViewModel

@Composable
fun HomeScreen(
    context: Context,
    productRepository: ProductRepository,
    onNavigateToAuth: () -> Unit
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val userViewModel: UserViewModel = hiltViewModel()
    val categoryViewModel: CategoryViewModel = hiltViewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // UI State for visibility
    val isMainScreen = currentRoute == ShopHomeScreen.DashboardScreen.route ||
            currentRoute == ShopHomeScreen.OrderScreen.route ||
            currentRoute == ShopHomeScreen.ProfileScreen.route

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            AnimatedVisibility(
                visible = isMainScreen,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                AppBar(
                    navController = navController,
                    isVisible = true,
                    categories = categoryViewModel.categories,
                    onCartIconClick = {
                        navController.navigate(DetailScreen.CartScreen.route)
                    },
                    onNotificationIconClick = {
                        navController.navigate(DetailScreen.NotificationScreen.route)
                    }
                )
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = isMainScreen,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                BottomNavBar(navController = navController)
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = isMainScreen,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                FloatingAddProductButton(navController = navController)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            HomeNavGraph(
                navHostController = navController,
                productRepository = productRepository,
                userViewModel = userViewModel,
                onNavigateToAuth = onNavigateToAuth,
            )
        }
    }
}
