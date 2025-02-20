package com.sutonglabs.tracestore.graphs.checkout_graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.sutonglabs.tracestore.graphs.Graph
import com.sutonglabs.tracestore.ui.add_address.AddAddressScreen
import com.sutonglabs.tracestore.ui.checkout_screen.CheckoutScreen
import com.sutonglabs.tracestore.ui.checkout_screen.OrderCreatedScreen
import com.sutonglabs.tracestore.ui.edit_address.EditAddressScreen

@Composable
fun CheckoutScreenWithContext(navController: NavHostController) {
    val context = LocalContext.current
    CheckoutScreen(context = context, navController = navController)
}

@Composable
fun EditAddressScreenWithContext(navController: NavHostController) {
    val context = LocalContext.current
    EditAddressScreen(navController = navController, context = context)
}

@Composable
fun AddAddressScreenWithContext(navController: NavHostController) {
    val context = LocalContext.current
    AddAddressScreen(navController = navController, context = context)
}



fun NavGraphBuilder.checkoutNavGraph(navController: NavHostController) {
    navigation(
        startDestination = "checkout_screen",
        route = Graph.CHECKOUT
    ) {
        composable("checkout_screen") {
            CheckoutScreenWithContext(navController)
        }
        composable(CheckoutScreen.EditAddressScreen.route) {
            EditAddressScreenWithContext(navController = navController)
        }
        composable(CheckoutScreen.AddAddressScreen.route) {
            AddAddressScreenWithContext(navController = navController)
        }
        composable(CheckoutScreen.OrderCreatedScreen.route) {
            OrderCreatedScreen(navController = navController)
        }
    }

}