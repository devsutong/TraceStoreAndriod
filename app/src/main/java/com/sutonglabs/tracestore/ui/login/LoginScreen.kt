package com.sutonglabs.tracestore.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sutonglabs.tracestore.graphs.auth_graph.AuthScreen
import com.sutonglabs.tracestore.viewmodels.UserViewModel

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect


@Composable
fun LoginScreen( navController: NavController, viewModel: UserViewModel = hiltViewModel()) {
    var username by remember { mutableStateOf("abc") }
    var password by remember { mutableStateOf("123") }
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
                val image = if (passwordVisibility)
                    Icons.Default.Visibility
                else Icons.Default.VisibilityOff

                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    Icon(imageVector = image, "")
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.login(username, password)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(text = "Not Registered Yet?")

        Button(onClick = { navController.navigate(AuthScreen.SignUpScreen.route)}) {
            Text(text = "Sign Up")
        }
        Spacer(modifier = Modifier.height(36.dp))


        loginState?.let { result ->
            if (result.isSuccess) {
                Text("Login successful! JWT: ${result.getOrNull()}")
                navController.navigate(AuthScreen.SignInSuccess.route)
            } else {
                LaunchedEffect(result) {
                    errorMessage = result.exceptionOrNull()?.message ?: "Unknown error"
                    showErrorDialog = true
                }
            }
        }
        jwtToken?.let { token ->
//            Text("JWT Token: $token")
        }
    }

    // Error Dialog
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            },
            title = { Text("Login Failed") },
            text = { Text(errorMessage) }
        )
    }
}
