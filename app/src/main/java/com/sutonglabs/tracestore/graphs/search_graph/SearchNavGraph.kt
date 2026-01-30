package com.sutonglabs.tracestore.graphs.search_graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.sutonglabs.tracestore.ui.search_screen.SearchScreen

const val SEARCH_ROUTE = "search"
private const val QUERY_ARG = "query"

fun NavGraphBuilder.searchNavGraph(
    navController: NavHostController
) {
    composable(
        route = "$SEARCH_ROUTE?$QUERY_ARG={$QUERY_ARG}",
        arguments = listOf(
            navArgument(QUERY_ARG) {
                type = NavType.StringType
                defaultValue = ""
            }
        )
    ) { backStackEntry ->
        val query = backStackEntry.arguments?.getString(QUERY_ARG).orEmpty()

        SearchScreen(
            initialQuery = query,
            navController = navController
        )
    }
}
