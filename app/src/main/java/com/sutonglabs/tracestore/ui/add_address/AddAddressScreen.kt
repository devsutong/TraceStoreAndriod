package com.sutonglabs.tracestore.ui.add_address

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sutonglabs.tracestore.api.request_models.CreateAddressRequest
import com.sutonglabs.tracestore.api.request_models.UpdateAddressRequest
import com.sutonglabs.tracestore.viewmodels.AddressViewModel

@Composable
fun AddAddressScreen(
    navController: NavController,
    addressViewModel: AddressViewModel = hiltViewModel(),
    context: Context
) {
    // Retrieve the current address from your view model state.
    val addressState = addressViewModel.state.value

    when {
        addressState.isLoading -> {
            // Display a loading indicator while waiting for data.
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
//        addressState.address.isNullOrEmpty() -> {
//            navController.popBackStack()
//        }
        else -> {

            var name by remember { mutableStateOf("") }
            var phoneNumber by remember { mutableStateOf("") }
            var pincode by remember { mutableStateOf("") }
            var city by remember { mutableStateOf("") }
            var stateName by remember { mutableStateOf("") }
            var locality by remember { mutableStateOf("") }
            var buildingName by remember { mutableStateOf("") }
            var landmark by remember { mutableStateOf("") }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = pincode,
                    onValueChange = { pincode = it },
                    label = { Text("Pincode") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = stateName,
                    onValueChange = { stateName = it },
                    label = { Text("State") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = locality,
                    onValueChange = { locality = it },
                    label = { Text("Locality") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = buildingName,
                    onValueChange = { buildingName = it },
                    label = { Text("Building Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = landmark,
                    onValueChange = { landmark = it },
                    label = { Text("Landmark") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        // Build an updated address request.
                        val addAddressRequest = CreateAddressRequest(
                            name = name,
                            phoneNumber = phoneNumber,
                            pincode = pincode,
                            city = city,
                            state = stateName,
                            locality = locality,
                            buildingName = buildingName,
                            landmark = landmark
                        )
                        // Call the update method on your AddressViewModel.
                        addressViewModel.createAddress(
                            context = context,
                            addAddressRequest = addAddressRequest
                        )
                        // Navigate back to the previous screen.
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Address")
                }
            }
        }
    }
}
