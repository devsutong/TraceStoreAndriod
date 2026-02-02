package com.sutonglabs.tracestore.ui.home_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.sutonglabs.tracestore.ui.home_screen.components.SearchBar
import androidx.compose.runtime.saveable.rememberSaveable
import com.google.android.material.search.SearchView
import com.sutonglabs.tracestore.models.Category
import com.sutonglabs.tracestore.ui.home_screen.components.CategoryStrip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    navController: NavController,
    isVisible: Boolean,
    onNotificationIconClick: () -> Unit,
    onCartIconClick: () -> Unit,
    categories: List<Category>,
    ) {

    var query by rememberSaveable { mutableStateOf("") }

    if (isVisible) {
        Column(
            modifier = Modifier.fillMaxWidth(),
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
                    SearchBar(
                        value = query,
                        onValueChange = { query = it },
                        onSearchSubmit = {
                            if (query.isNotBlank()) {
                                navController.navigate("search?query=$query")
                            }
                            query = ""
                        }
                    )

                }

            }

            CategoryStrip(
                categories = categories,
                onCategoryClick = { category ->
                    //TEST: use this for sql based categorisation
                    //navController.navigate("category-products/${category.id}")

                    //After doing research - sql bases categorization almost equal but searching is slower in sql
                    //resarch on postman
                    //TEST: use this for meilisearch based categorization
                    navController.navigate("search?query=${category.name}")
                }
            )
        }
    }
}