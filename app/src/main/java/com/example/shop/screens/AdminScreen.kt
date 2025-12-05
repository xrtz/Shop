package com.example.shop.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shop.R
import com.example.shop.pages.AddProductPage
import com.example.shop.pages.AdminOrdersPage
import com.example.shop.pages.AdminProfilePage
import com.example.shop.pages.BusketPage
import com.example.shop.pages.FavoritesPage
import com.example.shop.pages.ProductPage
import com.example.shop.pages.ProfilePage
import com.example.shop.pages.ShopPage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AdminScreen(modifier: Modifier = Modifier, navController: NavController) {
    Log.i("123", "adminScreen")

    val navItemList = listOf(
        NavItem("Orders", Icons.Default.ShoppingCart),
        NavItem("Products", Icons.Default.Info),
        NavItem("Add", Icons.Default.Add),
        NavItem("Profile", Icons.Default.AccountCircle)
    )
    var selectedBar by rememberSaveable {
        mutableStateOf(0)
    }
    Scaffold (
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = index == selectedBar,
                        onClick = {
                            selectedBar = index
                        },
                        icon = {
                            Icon(imageVector = navItem.icon, contentDescription = navItem.label)
                        },
                        label = {
                            Text(text = navItem.label)
                        }
                    )
                }
            }
        }
    ){

        ContentScreen2(modifier = Modifier.padding(it), selectedBar)
    }
}

@Composable
fun ContentScreen2(modifier: Modifier = Modifier, selectedBar: Int) {
    when(selectedBar){
        0-> AdminOrdersPage(modifier)
        1-> ProductPage(modifier)
        2-> AddProductPage(modifier)
        3-> AdminProfilePage(modifier)
    }
}

