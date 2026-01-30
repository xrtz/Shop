package com.example.shop.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.shop.GlobalNavigation
import com.example.shop.model.ProductModel
import com.example.shop.viewmodel.ShopViewModel

@Composable
fun BusketItemView(
    product: ProductModel,
    qty: Int,
    vm: ShopViewModel
) {
    val imageUrl = product.images.firstOrNull().orEmpty()
    val painter = rememberAsyncImagePainter(
        model = imageUrl
    )
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                GlobalNavigation.navController.navigate("product-details/${product.id}")
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painter,
                contentDescription = product.title,
                modifier = Modifier
                    .size(120.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Text(product.title, fontWeight = FontWeight.Bold)
                Text(product.actualPrice, fontSize = 16.sp)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = { vm.removeFromCart(product.id) }) {
                        Text("-", fontSize = 20.sp)
                    }
                    Text("$qty", fontSize = 16.sp)
                    TextButton(onClick = {vm.addToCart(product.id) }) {
                        Text("+", fontSize = 20.sp)
                    }
                }
            }

            IconButton(onClick = { vm.removeFromCart(product.id, true) }) {
                Icon(Icons.Default.Delete, contentDescription = "x")
            }
        }
    }
}
