package com.kottland.qrcanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kottland.qrcanner.ui.theme.QRcannerTheme
import com.kottland.qrcanner.view.GeneratorScreen
import com.kottland.qrcanner.view.HistoryScreen
import com.kottland.qrcanner.view.HomeScreen
import com.kottland.qrcanner.view.OnboardingScreen
import com.kottland.qrcanner.view.ScanResultScreen
import com.kottland.qrcanner.view.ScannerScreen
import com.kottland.qrcanner.view.SettingsScreen
import com.kottland.qrcanner.view.AboutUsScreen
import com.kottland.qrcanner.view.BatchScanScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QRcannerTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "onboarding") {
        composable("onboarding") {
            OnboardingScreen(navController = navController)
        }
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("scanner") {
            ScannerScreen(navController = navController)
        }
        composable("scan_result/{content}") { backStackEntry ->
            val content = backStackEntry.arguments?.getString("content") ?: ""
            ScanResultScreen(navController = navController, content = content)
        }
        composable("generator") {
            GeneratorScreen()
        }
        composable("batch_scan") {
            BatchScanScreen(navController = navController)
        }
        composable("history") {
            HistoryScreen()
        }
        composable("settings") {
            SettingsScreen()
        }
        composable("about_us") {
            AboutUsScreen(navController = navController)
        }
    }
}