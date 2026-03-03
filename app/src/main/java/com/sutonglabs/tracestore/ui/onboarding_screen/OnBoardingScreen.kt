package com.sutonglabs.tracestore.ui.onboarding_screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sutonglabs.tracestore.R
import com.sutonglabs.tracestore.graphs.auth_graph.AuthScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A splash screen that also serves as an onboarding experience for new users.
 * It features a multi-page horizontal pager to introduce the app's key features.
 *
 * @param navController The NavController for handling navigation to the login/signup screens.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SplashScreen(navController: NavController) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    // A list of pages to be displayed in the onboarding carousel.
    val onboardingPages = listOf(
        OnboardingPage(
            "Discover Products",
            "Explore a wide range of products from verified sellers around the globe.",
            Icons.Rounded.Storefront,
            MaterialTheme.colorScheme.primary
        ),
        OnboardingPage(
            "Blockchain Verified",
            "Every product is tracked on the blockchain for ultimate transparency and trust.",
            Icons.Rounded.Verified,
            Color(0xFF4CAF50)
        ),
        OnboardingPage(
            "Seamless Shopping",
            "Add to cart and checkout with ease. Your security is our priority.",
            Icons.Rounded.ShoppingBag,
            MaterialTheme.colorScheme.secondary
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // The main content pager.
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxWidth()
            ) { pageIndex ->
                OnboardingContent(onboardingPages[pageIndex])
            }

            // Bottom section containing the navigation controls and indicator.
            Column(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                DotIndicator(totalDots = 3, selectedIndex = pagerState.currentPage)

                Spacer(modifier = Modifier.height(48.dp))

                // Show "Next" button for the first two pages.
                if (pagerState.currentPage < 2) {
                    Button(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Next", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.Rounded.ArrowForward, contentDescription = "Next Page")
                    }
                } else {
                    // Show "Sign Up" and "Login" buttons on the last page.
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = { navController.navigate(AuthScreen.SignUpScreen.route) },
                            modifier = Modifier.weight(1f).height(56.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Sign Up", fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { navController.navigate(AuthScreen.SignInScreen.route) },
                            modifier = Modifier.weight(1f).height(56.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Login", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // "Skip" button to bypass the onboarding flow.
                TextButton(
                    onClick = { navController.navigate(AuthScreen.SignInScreen.route) },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Skip", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

/**
 * The content for a single onboarding page.
 *
 * @param page The OnboardingPage data to display.
 */
@Composable
fun OnboardingContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier.size(200.dp),
            shape = CircleShape,
            color = page.color.copy(alpha = 0.1f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = page.icon,
                    contentDescription = page.title,
                    modifier = Modifier.size(100.dp),
                    tint = page.color
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
    }
}

/**
 * Data class representing a single page in the onboarding flow.
 */
data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color
)
