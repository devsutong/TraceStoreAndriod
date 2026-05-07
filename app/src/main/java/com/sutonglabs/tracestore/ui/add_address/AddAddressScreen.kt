package com.sutonglabs.tracestore.ui.add_address

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sutonglabs.tracestore.api.request_models.CreateAddressRequest
import com.sutonglabs.tracestore.viewmodels.AddressViewModel

@Composable
fun AddAddressScreen(
    navController: NavController,
    context: Context,
    addressViewModel: AddressViewModel = hiltViewModel()
) {

    val addressState = addressViewModel.state.value

    val city by addressViewModel.city
    val stateName by addressViewModel.stateName
    val loadingLocation by addressViewModel.isFetchingLocation

    // ✅ FORM STATE
    var name by rememberSaveable { mutableStateOf("SATYA") }
    var phoneNumber by rememberSaveable { mutableStateOf("9801507928") }
    var pincode by rememberSaveable { mutableStateOf("") }
    var locality by rememberSaveable { mutableStateOf("NONW") }
    var buildingName by rememberSaveable { mutableStateOf("NONW") }
    var landmark by rememberSaveable { mutableStateOf("NONW") }

    when {

        addressState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        else -> {

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

                Spacer(Modifier.height(8.dp))

                TextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                TextField(
                    value = pincode,
                    onValueChange = {
                        pincode = it.take(6)

                        if (pincode.length == 6)
                            addressViewModel.onPincodeChanged(pincode)
                    },
                    label = { Text("Pincode") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (loadingLocation)
                    CircularProgressIndicator()

                Spacer(Modifier.height(8.dp))

                TextField(
                    value = city,
                    onValueChange = {},
                    enabled = false,
                    label = { Text("City") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                TextField(
                    value = stateName,
                    onValueChange = {},
                    enabled = false,
                    label = { Text("State") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                TextField(
                    value = locality,
                    onValueChange = { locality = it },
                    label = { Text("Locality") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                TextField(
                    value = buildingName,
                    onValueChange = { buildingName = it },
                    label = { Text("Building Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                TextField(
                    value = landmark,
                    onValueChange = { landmark = it },
                    label = { Text("Landmark") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {

                        val request = CreateAddressRequest(
                            name,
                            phoneNumber,
                            pincode,
                            city,
                            stateName,
                            locality,
                            buildingName,
                            landmark
                        )

                        addressViewModel.createAddress(request, context)

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