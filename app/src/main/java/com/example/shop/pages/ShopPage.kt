package com.example.shop.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shop.R
import com.example.shop.components.BannerView
import com.example.shop.components.CategoriesView
import com.example.shop.components.HeaderView
import com.example.shop.components.ProductItemView
import com.example.shop.model.ProductModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items

@Composable
fun ShopPage(modifier: Modifier = Modifier) {
    val productsList = remember{
        mutableStateOf<List<ProductModel>>(emptyList())
    }
    LaunchedEffect(Unit) {
        val snapshot = Firebase.firestore
            .collection("data")
            .document("stock")
            .collection("products")
            .get()
            .await()
        productsList.value = snapshot.documents.mapNotNull {
            it.toObject(ProductModel::class.java)
        }
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        HeaderView()
        Spacer(modifier = Modifier.height(10.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(8.dp)
        ){
            item(span = {GridItemSpan(2)}){
            BannerView(modifier = Modifier.height(200.dp))
        }
        item (span = {GridItemSpan(2)}){
            Spacer(Modifier.height(10.dp))
        }

        item(span = {GridItemSpan(2)}){
            Text(
                text = stringResource(R.string.word_Category),
                fontSize = 16.sp
            )
        }
        item(span = {GridItemSpan(2)}){
            CategoriesView()
        }
            items(
                items = productsList.value,
                key = {it.id}

            ){
                product ->
                ProductItemView(
                    product,
                    Modifier.padding(8.dp)
                )
            }
        }

    }
}