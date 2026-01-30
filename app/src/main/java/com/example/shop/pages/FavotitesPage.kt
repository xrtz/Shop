package com.example.shop.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shop.components.FavView
import com.example.shop.model.UserModel
import com.example.shop.repository.Repository
import com.example.shop.viewmodel.ShopViewModel
import com.example.shop.viewmodel.ShopViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun FavoritesPage(modifier: Modifier = Modifier) {

    val vm: ShopViewModel = viewModel(
        factory = ShopViewModelFactory(Repository())
    )

    val user by vm.user.collectAsState()
    val products by vm.products.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadProducts()
    }

    val favProducts = remember(user, products) {
        user?.favItems
            ?.mapNotNull { id -> products.find { it.id == id } }
            ?: emptyList()
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("Your fav", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        if (favProducts.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Empty")
            }
        } else {
            LazyColumn {
                items(favProducts, key = { it.id }) {
                    FavView(it, vm)
                }
            }
        }
    }
}
