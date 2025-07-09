package com.sutonglabs.tracestore.ui.admin.user_admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sutonglabs.tracestore.models.User
import com.sutonglabs.tracestore.viewmodels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserManagementScreen(
    navController: NavController,
    viewModel: UserViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val userList by viewModel.allUsers.collectAsState()
    val isLoading by viewModel.userLoading.collectAsState()
    val error by viewModel.userError.collectAsState()

    // Fetch users when the screen is opened
    LaunchedEffect(Unit) {
        viewModel.fetchAllUsers()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("User Management") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator()
                }

                error != null -> {
                    Text("Error: $error", color = MaterialTheme.colorScheme.error)
                }

                else -> {
                    if (userList.isEmpty()) {
                        Text("No users found.")
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(userList, key = { it.id }) { user ->
                                UserCard(user)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserCard(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("ID: ${user.id}", style = MaterialTheme.typography.bodySmall)
            Text("Username: ${user.username}", style = MaterialTheme.typography.titleMedium)
            Text("Email: ${user.email}")
            Text("Role: ${user.role}")
            Text("Name: ${user.firstName} ${user.lastName}")
            Text("Age: ${user.age}")
        }
    }
}
