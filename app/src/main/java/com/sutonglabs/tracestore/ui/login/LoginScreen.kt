package com.sutonglabs.tracestore.ui.login

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sutonglabs.tracestore.graphs.auth_graph.AuthScreen
import com.sutonglabs.tracestore.viewmodels.UserViewModel
import com.sutonglabs.tracestore.data.decodeJwt
import org.json.JSONObject

@Composable
fun LoginScreen(navController: NavController, viewModel: UserViewModel = hiltViewModel()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val loginState by viewModel.loginState.collectAsState()
    val jwtToken by viewModel.jwtToken.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            trailingIcon = {
                val image = if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(imageVector = image, contentDescription = null)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                Log.d("LoginScreen", "Login button clicked with username: $username")
                viewModel.login(username, password)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = "Not Registered Yet?")
        Button(onClick = {
            Log.d("LoginScreen", "Navigate to SignUp screen")
            navController.navigate(AuthScreen.SignUpScreen.route)
        }) {
            Text(text = "Sign Up")
        }
        Spacer(modifier = Modifier.height(36.dp))

        loginState?.let { result ->
            if (result.isSuccess) {
                Log.d("LoginScreen", "Login successful")

                // Get role from SharedPreferences (saved during login)
                val role = viewModel.getUserRole(LocalContext.current)
                Log.d("LoginScreen", "User role: $role")

                when (role) {
                    "admin" -> {
                        Log.d("LoginScreen", "Navigating to AdminDashboard")
                        navController.navigate(AuthScreen.AdminDashboard.route) {
                            popUpTo(AuthScreen.SignInScreen.route) { inclusive = true }
                        }
                    }
                    else -> {
                        Log.d("LoginScreen", "Navigating to SignInSuccess (user)")
                        navController.navigate(AuthScreen.SignInSuccess.route) {
                            popUpTo(AuthScreen.SignInScreen.route) { inclusive = true }
                        }
                    }
                }
            }  else {
                LaunchedEffect(result) {
                    errorMessage = result.exceptionOrNull()?.message ?: "Unknown error"
                    Log.e("LoginScreen", "Login failed: $errorMessage")
                    showErrorDialog = true
                }
            }
        }

        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                confirmButton = {
                    TextButton(onClick = { showErrorDialog = false }) {
                        Text("OK")
                    }
                },
                title = { Text("Login Error") },
                text = { Text(errorMessage) }
            )
        }
    }
}
