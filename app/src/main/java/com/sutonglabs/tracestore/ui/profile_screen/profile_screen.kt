package com.sutonglabs.tracestore.ui.profile_screen

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sutonglabs.tracestore.viewmodels.UserViewModel

/**
 * A composable function that represents the user profile screen.
 * This screen displays user information, quick links to orders and wishlist,
 * and a menu for editing profile, syncing blockchain, and logging out.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = hiltViewModel(),
    onBackBtnClick: () -> Unit,
    onNavigateToAuth: () -> Unit,
) {
    val context = LocalContext.current
    // Observe user info and JWT token from the ViewModel
    val userInfo by userViewModel.userInfo.collectAsState()
    val jwtToken by userViewModel.jwtToken.collectAsState()

    // Side effect to fetch user information whenever the JWT token changes or becomes available.
    LaunchedEffect(jwtToken) {
        jwtToken?.let { userViewModel.fetchUserInfo(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Account", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    // Back button to return to the previous screen
                    IconButton(onClick = onBackBtnClick) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Settings icon (currently a placeholder)
                    IconButton(onClick = { /* Settings placeholder */ }) {
                        Icon(Icons.Rounded.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()) // Enable scrolling for smaller screens
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Section 1: User Header
            // Displays profile picture, full name, and blockchain verification status.
            ProfileHeader(
                name = "${userInfo?.firstName ?: ""} ${userInfo?.lastName ?: ""}".trim().ifEmpty { userInfo?.username ?: "Guest" },
                email = userInfo?.email ?: "Not Available",
                isVerified = userInfo?.blockchainStatus ?: 0
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Section 2: Quick Action Links
            // Provides horizontal cards for rapid navigation to Orders, Wishlist, etc.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickLinkItem(Icons.Default.ShoppingBag, "Orders", Modifier.weight(1f)) {
                    navController.navigate("order_screen")
                }
                QuickLinkItem(Icons.Default.Favorite, "Wishlist", Modifier.weight(1f)) {
                    navController.navigate("favourite_screen")
                }
                QuickLinkItem(Icons.Default.LocationOn, "Addresses", Modifier.weight(1f)) {
                     // navController.navigate("address_screen")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Section 3: Main Menu List
            // A styled column containing actionable menu items with dividers.
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .shadow(2.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                // Allows user to modify their profile details
                ProfileMenuItem(Icons.Rounded.Person, "Edit Profile") {
                    navController.navigate("update_profile_screen")
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))

                // Navigation to the Seller Dashboard for managing listed products
                ProfileMenuItem(Icons.Rounded.Storefront, "Seller Hub (My Products)", tint = MaterialTheme.colorScheme.secondary) {
                    navController.navigate("seller_dashboard_screen")
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))

                Log.d("API_DEBUG", "Raw response: $userInfo")

                //todo -> not working, showing
                // Conditional item: Show "Sync Blockchain" only if the user is not yet verified
                if (userInfo?.blockchainStatus == 0) {
                    ProfileMenuItem(Icons.Rounded.VerifiedUser, "Sync Blockchain", tint = MaterialTheme.colorScheme.primary) {
                        userViewModel.syncAndRegister()
                    }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))
                }

                // Placeholder for managing notifications
                ProfileMenuItem(Icons.Rounded.Notifications, "Notifications") { }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))

                // Placeholder for support and help documents
                ProfileMenuItem(Icons.Rounded.Help, "Help & Support") { }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Section 4: Logout Action
            // Red-tinted button to clear session and navigate back to authentication.
            TextButton(
                onClick = {
                    userViewModel.logout(context)
                    onNavigateToAuth()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.AutoMirrored.Rounded.Logout, contentDescription = "Logout Icon")
                Spacer(modifier = Modifier.width(12.dp))
                Text("Logout", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

/**
 * Composable for the profile header section.
 * Renders the user's avatar, name, email, and a verification badge if applicable.
 */
@Composable
fun ProfileHeader(name: String, email: String, isVerified: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            // Profile Picture Circle
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Profile Placeholder",
                    modifier = Modifier.padding(20.dp).fillMaxSize(),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            // Verification checkmark badge
            if (isVerified == 1) {
                Surface(
                    modifier = Modifier.size(28.dp).shadow(2.dp, CircleShape),
                    shape = CircleShape,
                    color = Color(0xFF4CAF50)
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Verified Icon", modifier = Modifier.padding(4.dp), tint = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display Name and Email
        Text(text = name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(text = email, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

        // "Verified" label pill
        if (isVerified == 1) {
            Surface(
                modifier = Modifier.padding(top = 8.dp),
                color = Color(0xFFE8F5E9),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    "BLOCKCHAIN VERIFIED",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Composable for an individual quick link card.
 * Used for top-level summary actions like viewing orders or favorites.
 */
@Composable
fun QuickLinkItem(icon: ImageVector, label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.height(80.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Medium)
        }
    }
}

/**
 * Composable for an individual menu item row.
 * A horizontal row with an icon, text label, and a trailing chevron.
 */
@Composable
fun ProfileMenuItem(icon: ImageVector, label: String, tint: Color = MaterialTheme.colorScheme.onSurface, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = tint.copy(alpha = 0.7f), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, style = MaterialTheme.typography.bodyLarge, color = tint, modifier = Modifier.weight(1f))
        Icon(Icons.Rounded.ChevronRight, contentDescription = "Navigate")
    }
}
