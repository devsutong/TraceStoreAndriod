package com.sutonglabs.tracestore.ui.forgot_password

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.sutonglabs.tracestore.viewmodels.ForgotPasswordViewModel

@Composable
fun ResetPasswordScreen(
    navController: NavController,
    token: String,
    viewModel: ForgotPasswordViewModel = hiltViewModel()
) {

    var password by remember { mutableStateOf("") }

    val resetState by viewModel.resetState.collectAsState()

    Column(Modifier.padding(20.dp)) {

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("New Password") }
        )

        Button(onClick = {

            viewModel.resetPassword(token, password)

        }) {

            Text("Reset Password")

        }

    }

    resetState?.let {

        if (it.status) {

            navController.navigate("sign_in_screen") {

                popUpTo("sign_in_screen") { inclusive = true }

            }

            Toast.makeText(
                LocalContext.current,
                "Password changes sucessfully!!",
                Toast.LENGTH_SHORT
            ).show()

        }

    }

}