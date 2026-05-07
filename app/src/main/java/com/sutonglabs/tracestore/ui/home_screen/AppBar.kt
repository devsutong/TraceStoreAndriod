package com.sutonglabs.tracestore.ui.home_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.sutonglabs.tracestore.graphs.search_graph.SearchRoute
import com.sutonglabs.tracestore.graphs.subcategory_graph.SubcategoryRoute
import com.sutonglabs.tracestore.models.CategoryTree
import com.sutonglabs.tracestore.ui.home_screen.components.CategoryStrip
import com.sutonglabs.tracestore.ui.home_screen.components.QrScannerButton
import com.sutonglabs.tracestore.ui.home_screen.components.Search.SearchWithDropdown
import com.sutonglabs.tracestore.viewmodels.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    navController: NavController,
    isVisible: Boolean,
    onNotificationIconClick: () -> Unit,
    onCartIconClick: () -> Unit,
    categories: List<CategoryTree>,
    ) {

    if (isVisible) {

        val viewModel: SearchViewModel = hiltViewModel()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 1.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clickable {
                            onCartIconClick()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = "shopping cart")
                }
                ConstraintLayout() {
                    val (notification, notificationCounter) = createRefs()

                    Box(
                        modifier = Modifier
                            .constrainAs(notification) {}
                            .clickable {
                                onNotificationIconClick()
                            },

                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "notification"
                        )
                    }
                    //notification count
                    // TODO: fix notification server/android
                    Box(
                        modifier = Modifier
                            .background(color = Color.Red, shape = CircleShape)
                            .padding(1.dp)
                            .constrainAs(notificationCounter) {
                                top.linkTo(notification.top)
                                end.linkTo(notification.end)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "3", fontSize = 8.sp, color = Color.White)
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 1.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    //SEARCH WITH DROPDOWN
                    SearchWithDropdown(
                        viewModel = viewModel,
                        onSearchSubmit = { query ->
                            navController.navigate(SearchRoute.Search.createRoute(query))
                        }
                    )
                }

                QrScannerButton(
                    navController = navController
                )
            }

            CategoryStrip(
                categories = categories,
                onCategoryClick = { category ->
                    navController.navigate(SubcategoryRoute.Subcategory.createRoute(category.id))
                }
            )
        }
    }
}