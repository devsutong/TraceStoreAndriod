package com.sutonglabs.tracestore.ui.home_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sutonglabs.tracestore.graphs.detail_graph.DetailScreen
import com.sutonglabs.tracestore.graphs.home_graph.ShopHomeScreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add // <-- Added the import for Add icon

@Composable
fun NavigationBar(
    navController: NavHostController,
    isVisible: (Boolean) -> Unit
) {
    val navItemList = listOf(
        BottomNavItem.ProfileNav,
        BottomNavItem.HomeNav,
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var bottomNavVisibility by remember { mutableStateOf<Boolean>(true) }

    if (bottomNavVisibility) {
        NavigationBar(
            containerColor = Color.White,
            contentColor = Color.Black,
            tonalElevation = 1.dp,
            modifier = Modifier
                .height(60.dp)
                .background(color = Color.White)
                .shadow(
                    shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
                    elevation = 12.dp,
                )
                .clip(RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp))
        ) {
            navItemList.forEach { screen ->
                NavigationBarItem(
                    selected = screen.route == navBackStackEntry?.destination?.route,
                    icon = { Icon(imageVector = screen.icon, contentDescription = "icon") },
                    onClick = { navController.navigate(screen.route) },
                )
            }

            // Add the "+" icon for navigating to AddProductScreen
            NavigationBarItem(
                selected = false,
                icon = {
                    IconButton(onClick = { navController.navigate("add_product_screen") }) {
                        Icon(
                            imageVector = Icons.Filled.Add, // <-- Use the Add icon
                            contentDescription = "Add Product",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                onClick = {},
            )
        }
    }

    // Logic for hiding the bottom bar when on product details, cart, etc.
    when (navBackStackEntry?.destination?.route) {
        ShopHomeScreen.DashboardScreen.route -> {
            bottomNavVisibility = true
            isVisible(true)
        }
        DetailScreen.ProductDetailScreen.route -> {
            bottomNavVisibility = false
            isVisible(false)
        }
        DetailScreen.CartScreen.route -> {
            bottomNavVisibility = false
            isVisible(false)
        }
        DetailScreen.NotificationScreen.route -> {
            bottomNavVisibility = false
            isVisible(false)
        }
        else -> {
            bottomNavVisibility = true
            isVisible(false)
        }
    }
}
