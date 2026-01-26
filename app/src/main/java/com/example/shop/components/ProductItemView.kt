package com.example.shop.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.shop.GlobalNavigation
import com.example.shop.Util
import com.example.shop.model.ProductModel

@Composable
fun ProductItemView(product: ProductModel, modifier: Modifier = Modifier) {
    val imageUrl = product.images.firstOrNull().orEmpty()
    val painter = rememberAsyncImagePainter(
        model = imageUrl
    )


    var context = LocalContext.current
        Card(
            modifier = modifier.padding(8.dp).clickable {
                GlobalNavigation.navController.navigate("product-details/" + product.id)
            },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Image(
                    painter = painter,
                    contentDescription = product.title,
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = product.title, maxLines = 1,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(8.dp)
                    )
//                Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        Util.addToFav(context, productId = product.id)
                    }) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "+"
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.price,
                        fontSize = 14.sp,
                        style = TextStyle(textDecoration = TextDecoration.LineThrough)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = product.actualPrice,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
//                Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        Util.addToCard(context, productId = product.id)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "+"
                        )
                    }
                }
            }
        }
    }