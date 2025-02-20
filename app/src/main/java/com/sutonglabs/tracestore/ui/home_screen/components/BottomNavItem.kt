package com.sutonglabs.tracestore.ui.home_screen.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.sutonglabs.tracestore.graphs.home_graph.ShopHomeScreen

sealed class BottomNavItem(val title: String, val icon: ImageVector, val route: String) {
    object ProfileNav : BottomNavItem(
        title = "Profile",
        icon = Icons.Filled.Person,
        route = ShopHomeScreen.ProfileScreen.route
    )

    object FavouriteNav : BottomNavItem(
        title = "Favourite",
        icon = Icons.Filled.Favorite,
        route = ShopHomeScreen.FavouriteScreen.route
    )

    object ChatNav : BottomNavItem(
        title = "Chat",
        icon = Icons.Filled.ChatBubble,
        route = ShopHomeScreen.ConversationScreen.route
    )

    object HomeNav : BottomNavItem(
        title = "Home",
        icon = Icons.Filled.Home,
        route = ShopHomeScreen.DashboardScreen.route
    )

    object OrdersNav : BottomNavItem(
        title = "Orders",
        icon = Icons.Filled.ShoppingCart, // Using a cart icon for orders
        route = ShopHomeScreen.OrderScreen.route
    )
}
