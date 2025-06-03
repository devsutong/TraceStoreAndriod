package com.sutonglabs.tracestore.graphs.home_graph

sealed class ShopHomeScreen(val route: String) {
    object DashboardScreen : ShopHomeScreen("dashboard_screen")
    object ConversationScreen : ShopHomeScreen("conversation_screen")
    object ProfileScreen : ShopHomeScreen("profile_screen")
    object FavouriteScreen : ShopHomeScreen("favourite_screen")
    object OrderScreen :ShopHomeScreen("order_screen")
}
