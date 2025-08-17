package com.example.shop.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shop.model.OrderModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun AdminOrdersPage(modifier: Modifier = Modifier) {
    val orderList = remember{
        mutableStateOf<List<OrderModel>>(emptyList())
    }
    LaunchedEffect(key1 = Unit) {
        Firebase.firestore.collection("orders")
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
    Column (modifier = modifier.fillMaxSize().padding(16.dp)){
        Text(text = orderList.value.toString())
    }
}