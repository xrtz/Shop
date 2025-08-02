package com.example.shop.pages

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
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.unit.dp
import com.example.shop.components.ProductItemView
import com.example.shop.model.CategoryModel
import com.example.shop.model.ProductModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun CategoryProductPage(modifier: Modifier = Modifier, categoryId: String) {
    val productsList = remember{
        mutableStateOf<List<ProductModel>>(emptyList())
    }
    LaunchedEffect(key1 = Unit) {
        Firebase.firestore.collection("data").document("stock")
            .collection("products").whereEqualTo("category", categoryId).get().addOnCompleteListener {
                if (it.isSuccessful){
                    val resultList = it.result.documents.mapNotNull {
                            doc-> doc.toObject(ProductModel::class.java)
                    }
                    productsList.value = resultList.plus(resultList).plus(resultList)
                }
            }
    }
    LazyColumn (modifier = modifier.fillMaxSize().padding(24.dp)){
        items(productsList.value.chunked(2)){ rowItems ->
            Row{
                rowItems.forEach{
                    ProductItemView(product = it, modifier = Modifier.weight(1f))
                }
                if (rowItems.size == 1){
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

        }
    }
}