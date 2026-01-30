package com.example.shop.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shop.R
import com.example.shop.components.AdminProductItemView
import com.example.shop.components.BannerView
import com.example.shop.components.CategoriesView
import com.example.shop.components.HeaderView
import com.example.shop.components.ProductItemView
import com.example.shop.model.ProductModel
import com.example.shop.repository.Repository
import com.example.shop.viewmodel.ShopViewModel
import com.example.shop.viewmodel.ShopViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun AdminProductPage(modifier: Modifier = Modifier) {
    val vm: ShopViewModel = viewModel(
        factory = ShopViewModelFactory(Repository())
    )
    val productsList by vm.products.collectAsState()
    LaunchedEffect(Unit) {
        vm.loadProducts()
        vm.getUser()
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        HeaderView()
        Spacer(modifier = Modifier.height(10.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(8.dp)
        ){
            items(
                items = productsList,
                key = {it.id}

            ){
                    product ->
                AdminProductItemView(
                    product,
                    Modifier.padding(8.dp)
                )
            }
        }

    }
}