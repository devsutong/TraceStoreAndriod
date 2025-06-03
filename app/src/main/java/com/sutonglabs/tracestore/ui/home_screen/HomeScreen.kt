package com.sutonglabs.tracestore.ui.home_screen

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sutonglabs.tracestore.graphs.home_graph.HomeNavGraph
import com.sutonglabs.tracestore.graphs.detail_graph.DetailScreen
import com.sutonglabs.tracestore.repository.ProductRepository
import com.sutonglabs.tracestore.ui.home_screen.components.NavigationBar
import com.sutonglabs.tracestore.viewmodels.UserViewModel

@Composable
fun HomeScreen(
    context: Context,
    productRepository: ProductRepository,
    onNavigateToAuth: () -> Unit
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val topBarVisibilityState = remember { mutableStateOf(true) }

    // Obtain UserViewModel instance via Hilt
    val userViewModel: UserViewModel = hiltViewModel()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            AppBar(
                navController = navController,
                isVisible = topBarVisibilityState.value,
                searchCharSequence = {},
                onCartIconClick = {
                    navController.navigate(DetailScreen.CartScreen.route)
                },
                onNotificationIconClick = {
                    navController.navigate(DetailScreen.NotificationScreen.route)
                }
            )
        },
        bottomBar = {
            NavigationBar(navController = navController) { isVisible ->
                topBarVisibilityState.value = isVisible
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
