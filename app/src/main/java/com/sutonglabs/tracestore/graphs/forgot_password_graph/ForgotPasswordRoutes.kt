package com.sutonglabs.tracestore.graphs.forgot_password_graph

object ForgotPasswordRoutes {

    const val GRAPH = "forgot_password_graph"

    const val FORGOT_PASSWORD = "forgot_password"

    const val VERIFY_OTP = "verify_otp/{email}"

    const val RESET_PASSWORD = "reset_password/{resetToken}"

}