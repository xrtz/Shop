package com.example.shop.pages

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.shop.components.OrderItemView
import com.example.shop.model.OrderModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun AdminOrdersPage(modifier: Modifier = Modifier) {
    val orderList = remember { mutableStateOf<List<OrderModel>>(emptyList()) }

    LaunchedEffect(key1 = Unit) {
        Firebase.firestore.collection("orders")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val resultList = querySnapshot.documents.mapNotNull { doc ->
                    doc.toObject(OrderModel::class.java)?.copy(id = doc.id)
                }
                orderList.value = resultList
            }
            .addOnFailureListener { e ->
            }
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        if (orderList.value.isEmpty()) {
            Text("No orders found")
        } else {
            LazyColumn {
                items(orderList.value) { order ->
                    OrderCard(order = order, onStatusChanged = { newStatus ->
                        updateOrderStatus(order.id, newStatus) { success ->
                            if (success) {
                                orderList.value = orderList.value.map {
                                    if (it.id == order.id) it.copy(status = newStatus) else it
                                }
                            }
                        }
                    })
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: OrderModel, onStatusChanged: (String) -> Unit) {
    val radioOptions = listOf("ordered", "in transit", "ready")

    Card(modifier = Modifier.padding(vertical = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Order #${order.id}", fontWeight = FontWeight.Bold,
                maxLines = 1, overflow = TextOverflow.Ellipsis)

            Column(Modifier.selectableGroup()) {
                radioOptions.forEach { status ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (status == order.status),
                                onClick = { onStatusChanged(status) },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (status == order.status),
                            onClick = null
                        )
                        Text(status, Modifier.padding(start = 16.dp))
                    }
                }
            }

            Text("Date: ${order.date.toDate()}")
            Text("Address: ${order.address}")
            Text("User ID: ${order.userId}")
            Text("Items:", fontWeight = FontWeight.SemiBold)

            order.items.forEach { (productId, quantity) ->
                OrderItemView(productId = productId, qty = quantity)
            }
        }
    }
}

private fun updateOrderStatus(orderId: String, newStatus: String, onComplete: (Boolean) -> Unit) {
    Firebase.firestore.collection("orders")
        .document(orderId)
        .update("status", newStatus)
        .addOnSuccessListener { onComplete(true) }
        .addOnFailureListener { e ->
            onComplete(false)
        }
}