package com.example.shop.pages

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CategoryProductPage(modifier: Modifier = Modifier, categoryId: String) {
    Text(text = categoryId)
}