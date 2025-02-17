package com.sutonglabs.tracestore

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.sutonglabs.tracestore.api.TraceStoreAPI
import com.sutonglabs.tracestore.graphs.root_graph.RootNavigationGraph
import com.sutonglabs.tracestore.ui.theme.TraceStoreTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.sutonglabs.tracestore.repository.ProductRepository

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var apiService: TraceStoreAPI

    @Inject
    lateinit var productRepository: ProductRepository // Inject ProductRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TraceStoreTheme {
                ShowScreen(context = this)
            }
        }
    }

    @Composable
    private fun ShowScreen(context: Context) {
        val navHostController = rememberNavController()
        RootNavigationGraph(
            navHostController = navHostController,
            context = context,
            productRepository = productRepository // Pass productRepository
        )
    }
}
