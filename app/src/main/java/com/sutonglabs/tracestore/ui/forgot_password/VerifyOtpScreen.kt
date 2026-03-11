package com.sutonglabs.tracestore.ui.forgot_password

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.sutonglabs.tracestore.viewmodels.ForgotPasswordViewModel
import androidx.compose.ui.platform.LocalContext

@Composable
fun VerifyOtpScreen(
    navController: NavController,
    email: String,
    viewModel: ForgotPasswordViewModel = hiltViewModel()
) {

    var otp by remember { mutableStateOf("") }

    val verifyState by viewModel.verifyOtpState.collectAsState()

    Column(Modifier.padding(20.dp)) {

        OutlinedTextField(
            value = otp,
            onValueChange = { otp = it },
            label = { Text("OTP") }
        )

        Button(onClick = {

            viewModel.verifyOtp(email, otp)

        }) {

            Text("Verify OTP")

        }

    }


    verifyState?.let {

        if (it.status) {

            navController.navigate("reset_password/${it.resetToken}")

        }

        Toast.makeText(
        LocalContext.current,
        "OTP verified!!",
        Toast.LENGTH_SHORT
        ).show()

    }

}