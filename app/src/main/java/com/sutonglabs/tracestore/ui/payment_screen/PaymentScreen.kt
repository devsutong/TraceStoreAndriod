package com.sutonglabs.tracestore.ui.payment_screen

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sutonglabs.tracestore.viewmodels.CartViewModel
import com.sutonglabs.tracestore.viewmodels.OrderViewModel
import com.sutonglabs.tracestore.ui.checkout_screen.generateCreateOrderRequest
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    navController: NavController,
    context: Context,
    cartViewModel: CartViewModel = hiltViewModel(),
    orderViewModel: OrderViewModel = hiltViewModel()
) {
    val addressId = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<Int>("selectedAddressId") ?: 1

    val state = orderViewModel.state.value

    // State for selected payment method
    var selectedPaymentMethod by remember { mutableStateOf("COD") } // default: Cash on Delivery

    // Navigate only after success
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            navController.navigate("order_created_screen") {
                popUpTo("payment_screen") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Payment") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text("Select Payment Method", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(24.dp))

            // Payment options as RadioButtons
            val paymentOptions = listOf("Cash on Delivery", "Pay with UPI", "Pay with Tokens")

            paymentOptions.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            selectedPaymentMethod = option
                        }
                ) {
                    RadioButton(
                        selected = (selectedPaymentMethod == option),
                        onClick = { selectedPaymentMethod = option }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = option, style = MaterialTheme.typography.bodyLarge)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val request = generateCreateOrderRequest(cartViewModel, addressId)
                    if (request != null) {
                        // You might want to add payment method info in the request if backend supports it
                        orderViewModel.createOrder(request, context)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                Text(if (state.isLoading) "Placing Order..." else "Place Order")
            }

            Spacer(modifier = Modifier.height(16.dp))

            state.errorMessage?.let {
                Text("Error: $it", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

