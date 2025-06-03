package com.sutonglabs.tracestore.ui.onboarding_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sutonglabs.tracestore.R
import com.sutonglabs.tracestore.graphs.auth_graph.AuthScreen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val splashImageList = listOf(
        R.drawable.placeholder,
    )
    LaunchedEffect(Unit) {
        delay(1000)
        navController.navigate(AuthScreen.SignInScreen.route) {
            popUpTo(AuthScreen.OnBoardingScreen.route) {
                inclusive = true
            }
        }
    }
    val currentPosition = remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Spacer(modifier = Modifier.height(36.dp))
        DotIndicator(splashImageList.size, currentPosition.intValue)

        Spacer(modifier = Modifier.height(6.dp))

        Button(onClick = { navController.navigate(AuthScreen.SignInScreen.route)}) {
            Text(text = "Sign In")
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(text = "Not Registered Yet?")

        Button(onClick = { navController.navigate(AuthScreen.SignUpScreen.route)}) {
            Text(text = "Sign Up")
        }
        Spacer(modifier = Modifier.height(36.dp))

    }
}
