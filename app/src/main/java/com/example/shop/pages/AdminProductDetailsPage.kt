package com.example.shop.pages

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.shop.Util
import com.example.shop.model.CategoryModel
import com.example.shop.model.ProductModel
import com.example.shop.repository.Repository
import com.example.shop.viewmodel.ProductDetailsViewModel
import com.example.shop.viewmodel.ProductDetailsViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType
import kotlinx.coroutines.tasks.await

@Composable
fun AdminProductDetailsPage(productId: String,
                            modifier: Modifier = Modifier,
                            viewModel: ProductDetailsViewModel = viewModel(factory = ProductDetailsViewModelFactory(
                                Repository()
                            )
                            )
) {
    val product by viewModel.product.collectAsState()

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    val currentProduct = product ?: ProductModel()

    Column(modifier = modifier.padding(16.dp).fillMaxSize()) {
        Text(
            text = currentProduct.title,
            fontSize = 20.sp,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (currentProduct.images.isNotEmpty()) {
            val pagerState = rememberPagerState(0) { currentProduct.images.size }

            HorizontalPager(state = pagerState, pageSpacing = 24.dp) {
                AsyncImage(
                    model = currentProduct.images[it],
                    contentDescription = "",
                    modifier = Modifier.height(220.dp).fillMaxWidth().clip(RoundedCornerShape(16.dp))
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            DotsIndicator(
                dotCount = currentProduct.images.size,
                type = ShiftIndicatorType(
                    DotGraphic(
                        color = MaterialTheme.colorScheme.primary,
                        size = 6.dp
                    )
                ),
                pagerState = pagerState
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = currentProduct.price,
                fontSize = 18.sp,
                style = LocalTextStyle.current.copy(textDecoration = TextDecoration.LineThrough)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = currentProduct.actualPrice,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.weight(1f))
//            IconButton(onClick = { viewModel.toggleFavorite(currentProduct.id) }) {
//                Icon(
//                    imageVector = Icons.Default.FavoriteBorder,
//                    contentDescription = "Add to Favorites"
//                )
//            }
        }

        Spacer(modifier = Modifier.height(8.dp))

//        OutlinedButton(
//            onClick = { viewModel.addToCart(currentProduct.id) },
//            modifier = Modifier.fillMaxWidth().height(50.dp)
//        ) {
//            Text(text = "+")
//        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Description:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = currentProduct.description, fontSize = 18.sp)

        if (currentProduct.otherDetails.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Other details:", fontWeight = FontWeight.Bold)
            currentProduct.otherDetails.forEach { (key, value) ->
                Row(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                    Text(text = "$key: ", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = value, fontSize = 16.sp)
                }
            }
        }
    }
}