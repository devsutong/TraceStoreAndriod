package com.sutonglabs.tracestore.ui.home_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sutonglabs.tracestore.graphs.detail_graph.DetailScreen
import com.sutonglabs.tracestore.graphs.home_graph.ShopHomeScreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.ui.unit.sp

@Composable
fun NavigationBar(
    navController: NavController,
    isVisible: (Boolean) -> Unit
) {
    val navItemList = listOf(
        BottomNavItem.ProfileNav,
        BottomNavItem.HomeNav,
        BottomNavItem.OrdersNav
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var bottomNavVisibility by remember { mutableStateOf(true) }

    if (bottomNavVisibility) {
        NavigationBar(
            containerColor = Color.White,
            contentColor = MaterialTheme.colorScheme.primary,
            tonalElevation = 4.dp,
            modifier = Modifier
                .height(68.dp)
                .shadow(8.dp, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
        ) {
            navItemList.forEach { screen ->
                NavigationBarItem(
                    selected = screen.route == navBackStackEntry?.destination?.route,
                    icon = {
                        Icon(imageVector = screen.icon, contentDescription = screen.title)
                    },
                    label = {
                        Text(text = screen.title, fontSize = 12.sp)
                    },
                    onClick = { navController.navigate(screen.route) }
                )
            }
            NavigationBarItem(
                selected = navBackStackEntry?.destination?.route == "seller_dashboard_screen",
                icon = {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Seller Dashboard")
                },
                label = {
                    Text("Sell", fontSize = 12.sp)
                },
                onClick = {
                    navController.navigate("seller_dashboard_screen")
                }
            )

            NavigationBarItem(
                selected = navBackStackEntry?.destination?.route == "qr_choice",
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.QrCodeScanner,
                        contentDescription = "QR Scanner",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                label = {
                    Text("QR", fontSize = 12.sp)
                },
                onClick = {
                    navController.navigate("qr_choice")
                }
            )

        }
    }

    when (navBackStackEntry?.destination?.route) {
        ShopHomeScreen.DashboardScreen.route -> {
            bottomNavVisibility = true
            isVisible(true)
        }
        DetailScreen.ProductDetailScreen.route,
        DetailScreen.CartScreen.route,
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
