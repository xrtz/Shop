package com.example.shop.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shop.components.FavView
import com.example.shop.components.OrderItemView
import com.example.shop.model.OrderModel
import com.example.shop.model.ProductModel
import com.example.shop.model.UserModel
import com.example.shop.repository.Repository
import com.example.shop.viewmodel.OrderViewModel
import com.example.shop.viewmodel.OrderViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun OrderPage(modifier: Modifier = Modifier) {

    val vm: OrderViewModel = viewModel(
        factory = OrderViewModelFactory(Repository())
    )

    val orders by vm.orders.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadOrders()
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {

        Text("Your orders", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        if (orders.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Empty")
            }
        } else {
            LazyColumn {
                items(orders, key = { it.id }) { order ->
                    Card(Modifier.padding(vertical = 8.dp)) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Order #${order.id}", fontWeight = FontWeight.Bold)
                            Text("Status: ${order.status}")
                            Text("Date: ${order.date.toDate()}")

                            Spacer(Modifier.height(8.dp))

                            order.items.forEach { (id, qty) ->
                                OrderItemView(productId = id, qty = qty)
                            }
                        }
                    }
                }
            }
        }
    }
}
