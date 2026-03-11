package com.sutonglabs.tracestore.ui.home_screen.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sutonglabs.tracestore.graphs.home_graph.ShopHomeScreen

@Composable
fun BottomNavBar(
    navController: NavController,
) {
    val navItems = listOf(
        ShopHomeScreen.DashboardScreen,
        ShopHomeScreen.OrderScreen,
        ShopHomeScreen.ProfileScreen
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomAppBar(
        modifier = Modifier
            .height(64.dp)
            .clip(CircleShape),
        tonalElevation = 8.dp
    ) {
        navItems.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = { navController.navigate(screen.route) },
                icon = {
                    when (screen) {
                        ShopHomeScreen.DashboardScreen -> Icon(Icons.Default.Home, contentDescription = "Home")
                        ShopHomeScreen.OrderScreen -> Icon(Icons.Default.List, contentDescription = "Orders")
                        ShopHomeScreen.ProfileScreen -> Icon(Icons.Default.Person, contentDescription = "Profile")
                        else -> {}
                    }
                },
            )
        }
    }
}

@Composable
fun FloatingAddProductButton(navController: NavController) {
    FloatingActionButton(
        onClick = { navController.navigate("add_product_screen") },
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.primary,
    ) {
        Icon(Icons.Default.Add, contentDescription = "Add Product", tint = MaterialTheme.colorScheme.onPrimary)
    }
}
