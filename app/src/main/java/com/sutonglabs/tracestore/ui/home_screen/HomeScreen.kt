package com.sutonglabs.tracestore.ui.home_screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
                },
                onCameraIconClick = {
                    navController.navigate("qrScanner")
                }
            )
        },
        bottomBar = {
            NavigationBar(navController = navController) { isVisible ->
                topBarVisibilityState.value = isVisible
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                HomeNavGraph(
                    navHostController = navController,
                    productRepository = productRepository,
                    userViewModel = userViewModel,
                    onNavigateToAuth = onNavigateToAuth
                )
            }
        }
    }
}