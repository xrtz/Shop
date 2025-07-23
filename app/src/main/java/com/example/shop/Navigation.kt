package com.example.shop

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shop.screens.AuthScreen
import com.example.shop.screens.LoginScreen
import com.example.shop.screens.RegistrationScreen

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "auth") {
        composable("auth"){
            AuthScreen(modifier, navController)
        }
        composable("login"){
            LoginScreen(modifier)
        }
        composable("reg"){
            RegistrationScreen(modifier)
        }
    }
}