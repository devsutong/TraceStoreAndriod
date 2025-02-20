package com.sutonglabs.tracestore.graphs.checkout_graph

sealed class CheckoutScreen(val route: String) {
    data object EditAddressScreen : CheckoutScreen("edit_address_screen")
    data object AddAddressScreen : CheckoutScreen("add_address_screen")
    data object OrderCreatedScreen : CheckoutScreen("order_created_screen")


}