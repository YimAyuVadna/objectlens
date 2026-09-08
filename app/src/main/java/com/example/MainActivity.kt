package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CenterFocusStrong
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.navigation.NavRoutes
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.ScannerScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.ObjectLensTheme
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.HistoryViewModel
import com.example.ui.viewmodel.ScannerViewModel
import com.example.ui.viewmodel.SettingsViewModel
import com.example.util.PreferencesManager

data class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ObjectLensTheme {
                ObjectLensApp()
            }
        }
    }
}

@Composable
fun ObjectLensApp() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val preferences = remember { PreferencesManager(context) }

    val scannerViewModel: ScannerViewModel = viewModel()
    val historyViewModel: HistoryViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()

    val scannerUiState by scannerViewModel.uiState.collectAsState()
    val historyUiState by historyViewModel.uiState.collectAsState()
    val settingsUiState by settingsViewModel.uiState.collectAsState()

    val allScans by historyViewModel.scans.collectAsState()
    val allFavorites by historyViewModel.favorites.collectAsState()

    // Sync camera permission status on start
    LaunchedEffect(Unit) {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        scannerViewModel.setCameraPermission(hasPermission)
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        NavRoutes.HOME,
        NavRoutes.SCANNER,
        NavRoutes.HISTORY,
        NavRoutes.FAVORITES,
        NavRoutes.SETTINGS
    )

    val bottomNavItems = listOf(
        BottomNavItem(NavRoutes.HOME, "Home", Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavItem(NavRoutes.SCANNER, "Scanner", Icons.Filled.CenterFocusStrong, Icons.Outlined.CenterFocusStrong),
        BottomNavItem(NavRoutes.HISTORY, "History", Icons.Filled.History, Icons.Outlined.History),
        BottomNavItem(NavRoutes.FAVORITES, "Favorites", Icons.Filled.Star, Icons.Outlined.Star),
        BottomNavItem(NavRoutes.SETTINGS, "Settings", Icons.Filled.Settings, Icons.Outlined.Settings)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DarkBackground,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = DarkBackground,
                    tonalElevation = 0.dp,
                    modifier = Modifier.border(
                        androidx.compose.foundation.BorderStroke(1.dp, Color(0x14FFFFFF))
                    )
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = currentRoute == item.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.title,
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                indicatorColor = Color(0x1FFFFFFF),
                                unselectedIconColor = TextGray.copy(alpha = 0.6f),
                                unselectedTextColor = TextGray.copy(alpha = 0.6f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.SPLASH,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Splash Screen
            composable(NavRoutes.SPLASH) {
                SplashScreen(
                    onSplashFinished = {
                        val destination = if (preferences.hasCompletedOnboarding) {
                            NavRoutes.HOME
                        } else {
                            NavRoutes.ONBOARDING
                        }
                        navController.navigate(destination) {
                            popUpTo(NavRoutes.SPLASH) { inclusive = true }
                        }
                    }
                )
            }

            // Onboarding Screen
            composable(NavRoutes.ONBOARDING) {
                OnboardingScreen(
                    onComplete = { cameraPermissionGranted ->
                        preferences.hasCompletedOnboarding = true
                        scannerViewModel.setCameraPermission(cameraPermissionGranted)
                        navController.navigate(NavRoutes.HOME) {
                            popUpTo(NavRoutes.ONBOARDING) { inclusive = true }
                        }
                    }
                )
            }

            // Home Screen
            composable(NavRoutes.HOME) {
                HomeScreen(
                    recentScans = allScans,
                    favoritesCount = allFavorites.size,
                    onNavigateToScan = {
                        navController.navigate(NavRoutes.SCANNER) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToHistory = {
                        navController.navigate(NavRoutes.HISTORY) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onScanClick = { scan ->
                        historyViewModel.selectScan(scan)
                        historyViewModel.inspectObjectByName(scan.primaryObject)
                        navController.navigate(NavRoutes.HISTORY)
                    },
                    onObjectCategoryClick = { category ->
                        historyViewModel.onCategorySelected(category)
                        navController.navigate(NavRoutes.HISTORY)
                    },
                    onInspectObject = { objInfo ->
                        scannerViewModel.selectObjectByName(objInfo.name)
                        navController.navigate(NavRoutes.SCANNER)
                    }
                )
            }

            // Scanner Screen
            composable(NavRoutes.SCANNER) {
                ScannerScreen(
                    viewModel = scannerViewModel,
                    uiState = scannerUiState,
                    onNavigateToSettings = {
                        navController.navigate(NavRoutes.SETTINGS)
                    }
                )
            }

            // History Screen
            composable(NavRoutes.HISTORY) {
                HistoryScreen(
                    viewModel = historyViewModel,
                    uiState = historyUiState,
                    scans = allScans
                )
            }

            // Favorites Screen
            composable(NavRoutes.FAVORITES) {
                FavoritesScreen(
                    favorites = allFavorites,
                    onToggleFavorite = { info ->
                        historyViewModel.toggleFavorite(info)
                    }
                )
            }

            // Settings Screen
            composable(NavRoutes.SETTINGS) {
                SettingsScreen(
                    viewModel = settingsViewModel,
                    uiState = settingsUiState,
                    onSettingsChanged = {
                        scannerViewModel.updatePreferences()
                    }
                )
            }
        }
    }
}
