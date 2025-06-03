package com.sutonglabs.tracestore.ui.profile_screen

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import com.sutonglabs.tracestore.graphs.Graph
import com.sutonglabs.tracestore.graphs.auth_graph.AuthScreen
import com.sutonglabs.tracestore.viewmodels.UserViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = hiltViewModel(),
    onBackBtnClick: () -> Unit,
    onNavigateToAuth: () -> Unit,
) {
    // Access the context
    val context = LocalContext.current

    val userInfo by userViewModel.userInfo.collectAsState()

    // Get JWT token and fetch user info
    val jwtToken by userViewModel.jwtToken.collectAsState()
    LaunchedEffect(jwtToken) {
        jwtToken?.let {
            userViewModel.fetchUserInfo(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Icon
        Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(120.dp)
                .padding(8.dp)
        )

        // User Info Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProfileDetailRow("Username", userInfo?.username ?: "Unknown User")
                ProfileDetailRow("Email", userInfo?.email ?: "Not Available")
                ProfileDetailRow("First Name", userInfo?.firstName ?: "Not Available")
                ProfileDetailRow("Last Name", userInfo?.lastName ?: "Not Available")
                ProfileDetailRow("Age", userInfo?.age?.toString() ?: "Not Available")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Back Button
        Button(
            onClick = onBackBtnClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "Back", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        // Update Profile Button
        Button(
            onClick = {
                // Navigate to the update screen
                navController.navigate("update_profile_screen")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text(text = "Update Profile", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Logout Button (moved inside the Column)
        Button(
            onClick = {
                userViewModel.logout(context)

//                navController.navigate(Graph.AUTHENTICATION) {
//                    // Pop everything above the 'auth_graph' route
//                    popUpTo(Graph.ROOT) {
//                        inclusive = true
//                    }
//                    launchSingleTop = true
//                }

                onNavigateToAuth()

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text(
                text = "Logout",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

// Helper function for displaying profile details in a row format
@Composable
fun ProfileDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Text(text = value, fontSize = 16.sp)
    }
}
