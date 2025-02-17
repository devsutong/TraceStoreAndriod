package com.sutonglabs.tracestore.graphs.root_graph

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sutonglabs.tracestore.graphs.Graph
import com.sutonglabs.tracestore.graphs.auth_graph.authNavGraph
import com.sutonglabs.tracestore.ui.home_screen.HomeScreen
import com.sutonglabs.tracestore.repository.ProductRepository

@Composable
fun RootNavigationGraph(
    navHostController: NavHostController,
    context: Context,
    productRepository: ProductRepository // Accept productRepository here
) {
    NavHost(
        navController = navHostController,
        route = Graph.ROOT,
        startDestination = Graph.AUTHENTICATION,
    ) {
        // Authentication navigation graph
        authNavGraph(navHostController, context, productRepository) // Pass productRepository here

        // Home screen
        composable(route = Graph.HOME) {
            HomeScreen(context = context, productRepository = productRepository) // Pass productRepository here
        }
    }
}
