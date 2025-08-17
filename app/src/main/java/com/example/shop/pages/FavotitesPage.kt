package com.example.shop.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shop.GlobalNavigation
import com.example.shop.components.BusketItemView
import com.example.shop.components.FavView
import com.example.shop.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun FavoritesPage(modifier: Modifier = Modifier) {
    val userModel = remember{
        mutableStateOf(UserModel())
    }
    DisposableEffect(key1 = Unit) {
        var listener = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .addSnapshotListener() { it, _ ->
                if (it != null){
                    val result = it.toObject(UserModel::class.java)
                    if (result != null){
                        userModel.value = result}
                }
            }
        onDispose{
            listener.remove()
        }
    }
    Column(modifier = modifier.fillMaxSize().padding(16.dp)){
        Text(text = "Your fav", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        if (userModel.value.favItems.isNotEmpty())
        {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(userModel.value.favItems.toList()){
                    FavView(productId = it)
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