package com.sutonglabs.tracestore.ui.home_screen.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.sutonglabs.tracestore.graphs.home_graph.ShopHomeScreen

sealed class BottomNavItem(val title: String, val icon: ImageVector, val route: String) {
    object ProfileNav : BottomNavItem("Profile", Icons.Filled.Person, ShopHomeScreen.ProfileScreen.route)
    object FavouriteNav : BottomNavItem("Favourite", Icons.Filled.Favorite, ShopHomeScreen.FavouriteScreen.route)
    object ChatNav : BottomNavItem("Chat", Icons.Filled.ChatBubble, ShopHomeScreen.ConversationScreen.route)
    object HomeNav : BottomNavItem("Home", Icons.Filled.Home, ShopHomeScreen.DashboardScreen.route)
    object OrdersNav : BottomNavItem("Orders", Icons.Filled.ShoppingCart, ShopHomeScreen.OrderScreen.route)
}
