package com.example.shop.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.shop.GlobalNavigation
import com.example.shop.Util
import com.example.shop.model.ProductModel
import com.example.shop.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun OrderItemView(modifier: Modifier = Modifier, productId: String, qty: Long) {
    var product by remember {
        mutableStateOf(ProductModel())
    }
    LaunchedEffect(key1 = Unit) {
        Firebase.firestore.collection("data").document("stock")
            .collection("products").document(productId).get().addOnCompleteListener {
                if (it.isSuccessful){
                    val result = it.result.toObject(ProductModel::class.java)
                    if (result != null){
                        product = result
                    }
                }

            }
    }

    var context = LocalContext.current
    Card(
        modifier = modifier.padding(8.dp).fillMaxWidth().clickable {
            GlobalNavigation.navController.navigate("product-details/" + product.id)
        },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(8.dp)
    ){
        Row (modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(model = product.images.firstOrNull(),
                contentDescription = product.title,
                modifier = Modifier.height(120.dp).width(120.dp))
            Column (modifier = Modifier.padding(8.dp).weight(1f)){
                Text(text = product.title, maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis)


                Text(text = product.actualPrice,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp)

                Text(text = "Qty: ${qty}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp)
            }

        }
    }
}