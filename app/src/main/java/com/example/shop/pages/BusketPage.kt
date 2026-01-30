package com.example.shop.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shop.GlobalNavigation
import com.example.shop.components.BusketItemView
import com.example.shop.model.ProductModel
import com.example.shop.model.UserModel
import com.example.shop.repository.Repository
import com.example.shop.viewmodel.ShopViewModel
import com.example.shop.viewmodel.ShopViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

//@Composable
//fun BusketPage() {
//
//    val vm: ShopViewModel = viewModel(
//        factory = ShopViewModelFactory(Repository())
//    )
//
//    val products by vm.products.collectAsState()
//    LaunchedEffect(Unit) { vm.loadProducts() }
//
//    val user = remember { mutableStateOf(UserModel()) }
//
//    DisposableEffect(Unit) {
//        val l = Firebase.firestore.collection("users")
//            .document(FirebaseAuth.getInstance().currentUser!!.uid)
//            .addSnapshotListener { s, _ ->
//                s?.toObject(UserModel::class.java)?.let { user.value = it }
//            }
//        onDispose { l.remove() }
//    }
//
//    val cartItems = user.value.cartItems.toList()
//        .mapNotNull { (id, qty) ->
//            products.find { it.id == id }?.let { it to qty }
//        }
//
//    Column(Modifier.fillMaxSize().padding(16.dp)) {
//        Text("Your basket", fontSize = 20.sp, fontWeight = FontWeight.Bold)
//
//        if (cartItems.isEmpty()) {
//            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                Text("Empty")
//            }
//        } else {
//            LazyColumn(Modifier.weight(1f)) {
//                items(cartItems, key = { it.first.id }) {
//                    BusketItemView(it.first, it.second, vm)
//                }
//            }
//
//            OutlinedButton(
//                modifier = Modifier.fillMaxWidth().height(50.dp),
//                onClick = { GlobalNavigation.navController.navigate("checkout") }
//            ) {
//                Text("Checkout")
//            }
//        }
//    }
//}

@Composable
fun BusketPage(modifier: Modifier = Modifier) {
    val vm: ShopViewModel = viewModel(factory = ShopViewModelFactory(Repository()))
    val products by vm.products.collectAsState()
    val cart by vm.cartItems.collectAsState()

    val cartItems = cart.mapNotNull { (id, qty) ->
        products.find { it.id == id }?.let { it to qty }
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("Your basket", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        if (cartItems.isNotEmpty()) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartItems, key = { it.first.id }) {
                    BusketItemView(it.first, it.second, vm)
                }
            }

            OutlinedButton(
                modifier = Modifier.fillMaxWidth().height(50.dp),
                onClick = { GlobalNavigation.navController.navigate("checkout") }
            ) {
                Text("Checkout")
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Empty")
            }
        }
    }
}

