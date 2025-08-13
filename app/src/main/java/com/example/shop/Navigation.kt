package com.example.shop

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shop.pages.BusketPage
import com.example.shop.pages.CategoryProductPage
import com.example.shop.pages.CheckoutPage
import com.example.shop.pages.OrderPage
import com.example.shop.pages.ProductDetailsPage
import com.example.shop.screens.AuthScreen
import com.example.shop.screens.HomeScreen
import com.example.shop.screens.LoginScreen
import com.example.shop.screens.RegistrationScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    GlobalNavigation.navController = navController
    val isLoginIn = Firebase.auth.currentUser != null
    val firstpage = if(isLoginIn) "home" else "auth"
    NavHost(navController = navController, startDestination = firstpage) {
        composable("auth"){
            AuthScreen(modifier, navController)
        }
        composable("login"){
            LoginScreen(modifier, navController)
        }
        composable("reg"){
            RegistrationScreen(modifier, navController)
        }
        composable("home"){
            HomeScreen(modifier, navController)
        }
        composable("busket"){
            BusketPage(modifier)
        }
        composable("category-products/{categoryId}"){
            var categoryId = it.arguments?.getString("categoryId")
            CategoryProductPage(modifier, categoryId?:"")
        }
        composable("product-details/{productId}"){
            var productId = it.arguments?.getString("productId")
            ProductDetailsPage(modifier, productId?:"")
        }
        composable("checkout"){
            CheckoutPage(modifier)
        }
        composable("orders"){
            OrderPage(modifier)
        }
    }
}

object GlobalNavigation{
    lateinit var navController: NavHostController
}