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
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.sutonglabs.tracestore.graphs.home_graph.HomeNavGraph
import com.sutonglabs.tracestore.ui.home_screen.components.NavigationBar
import com.sutonglabs.tracestore.repository.ProductRepository
import androidx.hilt.navigation.compose.hiltViewModel
import com.sutonglabs.tracestore.graphs.detail_graph.DetailScreen

@Composable
fun HomeScreen(context: Context, productRepository: ProductRepository) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val topBarVisibilityState = remember { mutableStateOf(true) }

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
        // The content now fills the available height with consistent padding.
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            HomeNavGraph(navHostController = navController, productRepository = productRepository)
        }
    }
}
