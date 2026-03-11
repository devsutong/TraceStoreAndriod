package com.sutonglabs.tracestore.graphs.forgot_password_graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.sutonglabs.tracestore.ui.forgot_password.ForgotPasswordScreen
import com.sutonglabs.tracestore.ui.forgot_password.VerifyOtpScreen
import com.sutonglabs.tracestore.ui.forgot_password.ResetPasswordScreen


fun NavGraphBuilder.forgotPasswordNavGraph(
    navController: NavHostController
) {

    navigation(
        route = ForgotPasswordRoutes.GRAPH,
        startDestination = ForgotPasswordRoutes.FORGOT_PASSWORD
    ) {

        composable(ForgotPasswordRoutes.FORGOT_PASSWORD) {

            ForgotPasswordScreen(navController)
        }

        composable(
            route = ForgotPasswordRoutes.VERIFY_OTP,
            arguments = listOf(
                navArgument("email") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val email = backStackEntry.arguments?.getString("email") ?: ""

            VerifyOtpScreen(navController, email)
        }

        composable(
            route = ForgotPasswordRoutes.RESET_PASSWORD,
            arguments = listOf(
                navArgument("resetToken") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val token = backStackEntry.arguments?.getString("resetToken") ?: ""

            ResetPasswordScreen(navController, token )
        }

    }

}