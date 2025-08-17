package com.example.shop.pages

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shop.components.FavView
import com.example.shop.components.OrderItemView
import com.example.shop.model.OrderModel
import com.example.shop.model.ProductModel
import com.example.shop.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun OrderPage(modifier: Modifier = Modifier) {
    val orderList = remember{
        mutableStateOf<List<OrderModel>>(emptyList())
    }
    LaunchedEffect(key1 = Unit) {
        Firebase.firestore.collection("orders")
            .whereEqualTo("userId", FirebaseAuth.getInstance().currentUser?.uid!!)
            .get().addOnCompleteListener {
                if (it.isSuccessful){
                    val resultList = it.result.documents.mapNotNull {
                            doc-> doc.toObject(OrderModel::class.java)
                    }
                    if (resultList != null){
                        orderList.value = resultList
                    }
                }
            }
    }
    Column(modifier = modifier.fillMaxSize().padding(16.dp)){
        Text(text = "Your orders", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        if (orderList.value.isNotEmpty())
        {
            LazyColumn(/*modifier = Modifier.weight(1f)*/) {
                items(orderList.value.toList()){
                    Card(modifier = Modifier.padding(vertical = 8.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Order #${it.id}", fontWeight = FontWeight.Bold, maxLines = 1,overflow = TextOverflow.Ellipsis)
                            Text(text = "Status: ${it.status}")
                            Text(text = "Date: ${it.date.toDate()}")

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Items:", fontWeight = FontWeight.SemiBold)

                            it.items.forEach { (productId, quantity) ->
                                OrderItemView(productId = productId, qty = quantity)
                            }
                        }
                    }

                }
            }

        }
        else{
            Column (modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center){
                Text(text = "Empty")
            }
        }

    }
}