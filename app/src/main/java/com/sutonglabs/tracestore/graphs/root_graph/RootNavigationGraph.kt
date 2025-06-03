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
import com.sutonglabs.tracestore.graphs.home_graph.HomeNavGraph
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


        // Lambda to navigate to the authentication graph
        val navigateToAuthGraph = {
            navHostController.navigate(Graph.AUTHENTICATION) {
                // Clear the back stack up to the root to prevent going back to the authenticated part of the app
                popUpTo(Graph.ROOT) {
                    inclusive = true
                }
                // Ensure only one instance of the auth graph is launched
                launchSingleTop = true
            }
        }


        // Authentication navigation graph
        authNavGraph(navHostController, context, productRepository) // Pass productRepository here



        // Home screen
        composable(route = Graph.HOME) {
            HomeScreen(context = context, onNavigateToAuth = {
                navHostController.navigate(Graph.AUTHENTICATION) {
                    popUpTo(Graph.ROOT) { inclusive = true }
                }
            }, productRepository = productRepository) // Pass productRepository here
        }
    }
}
