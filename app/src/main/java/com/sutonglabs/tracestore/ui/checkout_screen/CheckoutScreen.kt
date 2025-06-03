package com.sutonglabs.tracestore.ui.checkout_screen

import android.annotation.SuppressLint
import androidx.compose.runtime.livedata.observeAsState

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sutonglabs.tracestore.api.request_models.CreateOrderRequest
import com.sutonglabs.tracestore.api.request_models.Product
import com.sutonglabs.tracestore.models.Address
import com.sutonglabs.tracestore.viewmodels.AddressViewModel
import com.sutonglabs.tracestore.viewmodels.CartViewModel
import com.sutonglabs.tracestore.viewmodels.OrderViewModel

@Composable
fun CheckoutScreen(
    navController: NavController,
    addressViewModel: AddressViewModel = hiltViewModel(),
    orderViewModel: OrderViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel(),
    context: Context
){
    val addressState = addressViewModel.state.value
    val orderState = orderViewModel.state.value

    val updatedFlag = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<Boolean>("addressUpdated")
        ?.observeAsState()

    LaunchedEffect(updatedFlag?.value) {
        updatedFlag?.value?.let { updated ->
            if (updated) {
                addressViewModel.getAddress()
                navController.currentBackStackEntry?.savedStateHandle?.remove<Boolean>("addressUpdated")
            }
        }
    }

    // Navigate to order created screen if order creation succeeded
    LaunchedEffect(orderState.isSuccess) {
        if (orderState.isSuccess) {
            navController.navigate("order_created_screen") {
                popUpTo("checkout_screen") { inclusive = true }
            }
            // Optionally reset order state here if you have implemented such function
            // orderViewModel.resetState()
        }
    }

    when {
        addressState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        addressState.address.isNullOrEmpty() -> {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Address Not Found!")
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        navController.navigate("add_address_screen")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Add Address")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        else -> {
            val address = addressState.address[0]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                AddressCard(
                    address = address,
                    navController = navController,
                    name = address.name,
                    phoneNumber = address.phoneNumber ?: "N/A",
                    pincode = address.pincode ?: "N/A",
                    city = address.city ?: "N/A",
                    stateName = address.state ?: "N/A",
                    locality = address.locality ?: "N/A",
                    buildingName = address.buildingName ?: "N/A",
                    landmark = address.landmark ?: "N/A",
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        val request = generateCreateOrderRequest(cartViewModel, address.id)
                        if (request != null) {
                            orderViewModel.createOrder(request, context)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Confirm Order")
                }

                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun AddressCard(
    address: Address,
    navController: NavController,
    name: String,
    phoneNumber: String,
    pincode: String,
    city: String,
    stateName: String,
    locality: String,
    buildingName: String,
    landmark: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Address",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )

            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Phone: $phoneNumber",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Address: ${buildingName}, ${locality}, $city - $pincode",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "State: $stateName",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Landmark: $landmark",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    // Navigate to the EditAddressScreen passing the address ID.
                    address.let {
                        navController.navigate("edit_address_screen")
                    }
                },
            ) {
                Text(text = "Edit Address")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Text(
                    text = "Payment: COD",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
fun generateCreateOrderRequest(cartViewModel: CartViewModel, addressID: Int): CreateOrderRequest? {
    val products = cartViewModel.state.value.product?.map {
        Product(
            productID = it.product.id,  // Use 'it.product.id' to match DB ID
            quantity = it.quantity
        )
    }

    products?.forEach {
        Log.d("CheckoutDebug", "Sending productID: ${it.productID}, quantity: ${it.quantity}")
    }

    val totalAmount = cartViewModel.state.value.product?.sumOf {
        (it.product.price.toDouble()) * it.quantity.toDouble()
    } ?: 0.0

    return products?.let {
        CreateOrderRequest(
            products = it,
            totalAmount = totalAmount,
            addressID = addressID,
            status = "Success"
        )
    }
}

