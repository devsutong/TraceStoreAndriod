package com.sutonglabs.tracestore.ui.qrscanner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRChoiceScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("QR Options") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    navController.navigate("enter_uid")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enter UID")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate("qrScanner")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Scan QR Code")
            }
        }
    }
}
