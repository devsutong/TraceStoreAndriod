package com.sutonglabs.tracestore.ui.update_profile_screen

import android.widget.Toast
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sutonglabs.tracestore.viewmodels.UserViewModel

@Composable
fun UpdateProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = hiltViewModel()
) {
    val userInfo by userViewModel.userInfo.collectAsState()
    val updateState by userViewModel.updateState.collectAsState()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Update text fields when userInfo is available
    LaunchedEffect(userInfo) {
        userInfo?.let {
            firstName = it.firstName
            lastName = it.lastName
            age = it.age.toString()
        }
    }

    // Handle update result
    LaunchedEffect(updateState) {
        updateState?.let { result ->
            Log.d("UpdateProfile", "Update result received: $result")
            isLoading = false
            result.onSuccess {
                Log.d("UpdateProfile", "Profile updated successfully!")
                Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }.onFailure {
                Log.d("UpdateProfile", "Error updating profile: ${it.message}")
                Toast.makeText(context, "Error updating profile: ${it.message}", Toast.LENGTH_SHORT).show()
            }
            userViewModel.resetUpdateState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Update Profile", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                Log.d("UpdateProfile", "Save Changes button clicked.")
                if (firstName.isNotBlank() && lastName.isNotBlank() && age.isNotBlank()) {
                    val ageInt = age.toIntOrNull()
                    if (ageInt != null) {
                        Log.d("UpdateProfile", "Valid input, updating profile.")
                        isLoading = true
                        userViewModel.updateUserProfile(firstName, lastName, ageInt)
                    } else {
                        Log.d("UpdateProfile", "Invalid age input: $age")
                        Toast.makeText(context, "Age must be a valid number", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d("UpdateProfile", "One or more fields are blank.")
                    Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Save Changes")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}
