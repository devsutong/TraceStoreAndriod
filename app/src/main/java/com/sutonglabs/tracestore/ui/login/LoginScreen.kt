package com.sutonglabs.tracestore.ui.login

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sutonglabs.tracestore.graphs.auth_graph.AuthScreen
import com.sutonglabs.tracestore.graphs.forgot_password_graph.ForgotPasswordRoutes
import com.sutonglabs.tracestore.viewmodels.UserViewModel

/**
 * A composable function that represents the login screen of the application.
 * This screen provides UI for user authentication, including fields for username and password,
 * a login button, and options for password recovery and signing up.
 *
 * @param navController The NavController used for navigating between screens.
 * @param viewModel The UserViewModel used for handling user-related logic, such as login.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, viewModel: UserViewModel = hiltViewModel()) {
    // State for the username and password fields. `remember` is used to maintain state across recompositions.
    var username by remember { mutableStateOf("abc") } // Initially empty for production
    var password by remember { mutableStateOf("123456") } // Initially empty for production

    // State for toggling password visibility (showing or hiding the password characters).
    var passwordVisibility by remember { mutableStateOf(false) }

    // State for showing or hiding an error dialog when login fails.
    var showErrorDialog by remember { mutableStateOf(false) }
    // State to hold the error message to be displayed in the dialog.
    var errorMessage by remember { mutableStateOf("") }

    // Collect the login state from the ViewModel as a State object.
    // This allows the UI to react to changes in the login process (e.g., success or failure).
    val loginState by viewModel.loginState.collectAsState()

    // The main container for the login screen, with a gradient background.
    Box(
        modifier = Modifier
            .fillMaxSize() // The Box will fill the entire screen.
            .background(
                // Apply a vertical gradient background.
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), // Start color with slight transparency.
                        MaterialTheme.colorScheme.background // End color from the material theme.
                    )
                )
            )
    ) {
        // A Column to arrange UI elements vertically.
        Column(
            modifier = Modifier
                .fillMaxSize() // The Column will also fill the entire screen.
                .padding(24.dp) // Apply padding around the content.
                .statusBarsPadding(), // Add padding to avoid overlapping with the status bar.
            horizontalAlignment = Alignment.CenterHorizontally, // Center children horizontally.
            verticalArrangement = Arrangement.Center // Center children vertically.
        ) {
            // App Logo or Icon Area.
            Surface(
                modifier = Modifier.size(100.dp), // Set a fixed size for the logo container.
                shape = RoundedCornerShape(24.dp), // Apply rounded corners.
                color = MaterialTheme.colorScheme.primary, // Use the primary color for the background.
                tonalElevation = 8.dp // Add a shadow effect.
            ) {
                Icon(
                    imageVector = Icons.Default.Store, // The store icon.
                    contentDescription = "Store Icon", // Accessibility description.
                    modifier = Modifier.padding(24.dp), // Padding inside the Surface.
                    tint = MaterialTheme.colorScheme.onPrimary // Icon color that contrasts with the primary color.
                )
            }

            Spacer(modifier = Modifier.height(32.dp)) // Add vertical space.

            // Welcome Text Section.
            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.displaySmall, // Use a large text style.
                fontWeight = FontWeight.Bold, // Make the text bold.
                color = MaterialTheme.colorScheme.onBackground // Text color that contrasts with the background.
            )
            Text(
                text = "Sign in to continue shopping",
                style = MaterialTheme.typography.bodyLarge, // Use a medium text style.
                color = MaterialTheme.colorScheme.onSurfaceVariant // A slightly muted text color.
            )

            Spacer(modifier = Modifier.height(48.dp)) // Add more vertical space.

            // Username Input Field.
            OutlinedTextField(
                value = username, // The current value of the username.
                onValueChange = { username = it }, // Update the username state when the user types.
                label = { Text("Username") }, // The label for the text field.
                modifier = Modifier.fillMaxWidth(), // The text field will take the full width.
                leadingIcon = { Icon(Icons.Rounded.Person, contentDescription = "Username Icon") }, // An icon to the left of the text.
                shape = RoundedCornerShape(12.dp), // Rounded corners for the text field border.
                singleLine = true, // The input will be on a single line.
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next) // Show a "Next" button on the keyboard.
            )

            Spacer(modifier = Modifier.height(16.dp)) // Add vertical space.

            // Password Input Field.
            OutlinedTextField(
                value = password, // The current value of the password.
                onValueChange = { password = it }, // Update the password state when the user types.
                label = { Text("Password") }, // The label for the text field.
                modifier = Modifier.fillMaxWidth(), // The text field will take the full width.
                leadingIcon = { Icon(Icons.Rounded.Lock, contentDescription = "Password Icon") }, // An icon to the left of the text.
                shape = RoundedCornerShape(12.dp), // Rounded corners for the text field border.
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(), // Hide or show the password.
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done), // Use a password keyboard and a "Done" button.
                singleLine = true, // The input will be on a single line.
                trailingIcon = { // An icon to the right of the text.
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) { // Toggle password visibility on click.
                        Icon(
                            imageVector = if (passwordVisibility) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff, // Change icon based on visibility.
                            contentDescription = "Toggle Password Visibility" // Accessibility description.
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp)) // Add a small vertical space.

            // "Forgot Password?" Text.
            TextButton(
                onClick = {
                    navController.navigate(ForgotPasswordRoutes.FORGOT_PASSWORD)
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = "Forgot Password?",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp)) // Add vertical space.

            // Login Button.
            Button(
                onClick = { viewModel.login(username, password) }, // Trigger the login function in the ViewModel.
                modifier = Modifier
                    .fillMaxWidth() // The button will take the full width.
                    .height(56.dp), // Set a fixed height for the button.
                shape = RoundedCornerShape(12.dp), // Rounded corners for the button.
                elevation = ButtonDefaults.buttonElevation(4.dp) // Add a shadow effect.
            ) {
                Text("SIGN IN", fontWeight = FontWeight.Bold, fontSize = 16.sp) // The text inside the button.
            }

            Spacer(modifier = Modifier.height(24.dp)) // Add vertical space.

            // Sign Up Link Section.
            Row(
                modifier = Modifier.fillMaxWidth(), // The Row will take the full width.
                horizontalArrangement = Arrangement.Center, // Center children horizontally.
                verticalAlignment = Alignment.CenterVertically // Center children vertically.
            ) {
                Text(
                    text = "Don't have an account? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(onClick = { navController.navigate(AuthScreen.SignUpScreen.route) }) { // Navigate to the Sign Up screen on click.
                    Text(
                        text = "Sign Up",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Handle the login state changes observed from the ViewModel.
        loginState?.let { result ->
            if (result.isSuccess) {
                // On successful login, navigate to the success screen.
                // `LaunchedEffect` is used to run a side effect (navigation) in a coroutine scope
                // that is tied to the lifecycle of the composable.
                LaunchedEffect(Unit) {
                    navController.navigate(AuthScreen.SignInSuccess.route) {
                        // Pop up to the sign-in screen to remove it from the back stack,
                        // preventing the user from navigating back to it after logging in.
                        popUpTo(AuthScreen.SignInScreen.route) { inclusive = true }
                    }
                }
            } else {
                // On login failure, show an error dialog.
                // `LaunchedEffect` with `result` as a key ensures this block runs whenever the login result changes to a failure.
                LaunchedEffect(result) {
                    errorMessage = result.exceptionOrNull()?.message ?: "Unknown error" // Get the error message.
                    showErrorDialog = true // Set the state to show the dialog.
                }
            }
        }
    }

    // Composable for the Error Dialog.
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false }, // Hide the dialog when the user clicks outside of it.
            confirmButton = {
                Button(onClick = { showErrorDialog = false }) { // Hide the dialog on button click.
                    Text("Try Again")
                }
            },
            title = { Text("Authentication Failed") }, // The title of the dialog.
            text = { Text(errorMessage) }, // The error message.
            shape = RoundedCornerShape(24.dp) // Rounded corners for the dialog.
        )
    }
}
