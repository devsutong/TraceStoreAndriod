package com.sutonglabs.tracestore.ui.profile_screen

import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sutonglabs.tracestore.viewmodels.UserViewModel

@Composable
fun ProfileScreen(
    userViewModel: UserViewModel = hiltViewModel(),
    onBackBtnClick: () -> Unit
) {
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

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(onBackBtnClick = {})
}
