package com.sutonglabs.tracestore.graphs.category_products_graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.sutonglabs.tracestore.ui.category_product_screen.CategoryProductsScreen

const val CATEGORY_PRODUCTS_ROUTE = "category-products"

fun NavGraphBuilder.categoryProductsNavGraph(
    navController: NavHostController
) {
    composable(
        route = "$CATEGORY_PRODUCTS_ROUTE/{categoryId}",
        arguments = listOf(
            navArgument("categoryId") {
                type = NavType.IntType
            }
        )
    ) { backStackEntry ->
        val categoryId = backStackEntry.arguments
            ?.getInt("categoryId")
            ?: return@composable

        CategoryProductsScreen(
            navController = navController,
            categoryId = categoryId
        )
    }
}
