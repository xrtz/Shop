package com.example.shop.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.shop.GlobalNavigation
import com.example.shop.model.ProductModel
import com.example.shop.viewmodel.ShopViewModel

@Composable
fun FavView(
    product: ProductModel,
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
                Text(
                    product.title,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis
                )
                Text(product.actualPrice, fontSize = 16.sp)

                IconButton(onClick = { vm.addToCart(product.id) }) {
                    Icon(Icons.Default.Add, contentDescription = "+")
                }
            }

            IconButton(onClick = { vm.toggleFavorite(product.id) }) {
                Icon(Icons.Default.Delete, contentDescription = "x")
            }
        }
    }
}
