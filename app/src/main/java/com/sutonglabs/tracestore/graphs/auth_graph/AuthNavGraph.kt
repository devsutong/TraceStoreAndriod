package com.sutonglabs.tracestore.graphs.auth_graph

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.sutonglabs.tracestore.graphs.Graph
import com.sutonglabs.tracestore.ui.home_screen.HomeScreen
import com.sutonglabs.tracestore.ui.login.LoginScreen
import com.sutonglabs.tracestore.ui.onboarding_screen.SplashScreen
import com.sutonglabs.tracestore.ui.signup.RegisterScreen
import com.sutonglabs.tracestore.repository.ProductRepository

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    context: Context,
    productRepository: ProductRepository // Accept productRepository here
) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreen.OnBoardingScreen.route
    ) {
        // Splash Screen
        composable(route = AuthScreen.OnBoardingScreen.route) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                (context as Activity).window.decorView.windowInsetsController?.hide(
                    WindowInsets.Type.statusBars()
                )
            } else {
                (context as Activity).window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
            SplashScreen(navController)
            Log.d("Navigation Call", "Called Splash Screen")
        }

        // Login Screen
        composable(AuthScreen.SignInScreen.route) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                (context as Activity).window.decorView.windowInsetsController?.show(
                    WindowInsets.Type.statusBars()
                )
            } else {
                (context as Activity).window.apply {
                    clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                }
            }
            LoginScreen(navController = navController)
        }

        // Registration Screen
        composable(AuthScreen.SignUpScreen.route) {
            RegisterScreen(navController = navController)
        }

        // Home Screen after Sign In Success
        composable(AuthScreen.SignInSuccess.route) {
            HomeScreen(context = context, onNavigateToAuth = {
                navController.navigate(Graph.AUTHENTICATION) {
                    popUpTo(Graph.ROOT) { inclusive = true }
                }
            },  productRepository = productRepository) // Pass productRepository here
        }
    }
}
