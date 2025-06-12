package com.sutonglabs.tracestore.ui.qrscanner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterUIDScreen(navController: NavController) {
    var uid by remember { mutableStateOf(TextFieldValue()) }
    val isValid = uid.text.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Enter UID") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Please enter the UID of the product", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = uid,
                onValueChange = { uid = it },
                placeholder = { Text("e.g., 1234567890") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Navigate or trigger logic with the UID
                    navController.navigate("product_details_from_uid/${uid.text}")
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isValid
            ) {
                Text("Submit")
            }
        }
    }
}
