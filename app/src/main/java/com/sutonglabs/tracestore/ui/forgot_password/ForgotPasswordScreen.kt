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
fun ForgotPasswordScreen(
    navController: NavController,
    viewModel: ForgotPasswordViewModel = hiltViewModel()
) {

    var email by remember { mutableStateOf("abc@gmail.com") }

    val context = LocalContext.current
    val otpState by viewModel.otpState.collectAsState()

    Column(Modifier.padding(20.dp)) {

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {

            viewModel.requestOtp(email)

        }) {

            Text("Send OTP")

        }

    }

    otpState?.let {

        if (it.status) {

            navController.navigate("verify_otp/$email")

            Toast.makeText(
                context,
                "OTP sent to $email",
                Toast.LENGTH_SHORT
            ).show()

        } else {

            Toast.makeText(
                context,
                 it.error ?: "Something went wrong",
                Toast.LENGTH_SHORT
            ).show()

        }

    }

}