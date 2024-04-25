package com.alekseeva.smallapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.alekseeva.smallapp.AppNavHost
import com.alekseeva.smallapp.ui.theme.SmallAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmallAppTheme {
                AppNavHost(navController = rememberNavController())
            }
        }
    }
}