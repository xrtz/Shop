package com.example.shop.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shop.R
import com.example.shop.components.AdminProductItemView
import com.example.shop.components.BannerView
import com.example.shop.components.CategoriesView
import com.example.shop.components.HeaderView
import com.example.shop.components.ProductItemView
import com.example.shop.model.ProductModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun AdminProductPage(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        HeaderView(modifier)
        val productsList = remember{
            mutableStateOf<List<ProductModel>>(emptyList())
        }
        LaunchedEffect(key1 = Unit) {
            Firebase.firestore.collection("data").document("stock")
                .collection("products").get().addOnCompleteListener {
                    if (it.isSuccessful){
                        val resultList = it.result.documents.mapNotNull {
                                doc-> doc.toObject(ProductModel::class.java)
                        }
                        productsList.value = resultList.plus(resultList).plus(resultList)
                    }
                }
        }
        LazyColumn (modifier = Modifier.fillMaxSize().padding(4.dp)){
            items(productsList.value.chunked(2)){ rowItems ->
                Row{
                    rowItems.forEach{
                        AdminProductItemView(product = it, modifier = Modifier.weight(1f))
                    }
                    if (rowItems.size == 1){
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}