package com.example.shop.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shop.R
import com.example.shop.components.BannerView
import com.example.shop.components.CategoriesView
import com.example.shop.components.HeaderView

@Composable
fun ShopPage(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        HeaderView(modifier)
        Spacer(modifier = Modifier.height(10.dp))
        BannerView(modifier = Modifier.height(200.dp))
        Text(text = stringResource(R.string.word_Category), style = TextStyle(fontSize = 16.sp))
        Spacer(modifier = Modifier.height(10.dp))
        CategoriesView(modifier)
    }
}