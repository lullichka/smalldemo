package com.alekseeva.smallapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alekseeva.smallapp.home.HomeScreen
import com.alekseeva.smallapp.editprofile.EditProfileScreen

class AppNavigation {
    enum class Screen {
        HOME,
        EDIT_USER,
    }

    sealed class NavigationItem(val route: String) {
        data object Home : NavigationItem(Screen.HOME.name)
        data object EditUser : NavigationItem(Screen.EDIT_USER.name)
    }
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = AppNavigation.NavigationItem.Home.route,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(AppNavigation.NavigationItem.Home.route) {
            HomeScreen(navController = navController, hiltViewModel())
        }
        composable(AppNavigation.NavigationItem.EditUser.route) {
            EditProfileScreen(navController = navController, hiltViewModel())
        }
    }
}