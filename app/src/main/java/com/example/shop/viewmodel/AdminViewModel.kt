package com.example.shop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.model.CategoryModel
import com.example.shop.model.ProductModel
import com.example.shop.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class AdminViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<CategoryModel>>(emptyList())
    val categories: StateFlow<List<CategoryModel>> = _categories

    fun loadCategories() {
        viewModelScope.launch {
            _categories.value = repository.getCategories()
        }
    }

    fun addProduct(
        name: String,
        description: String,
        category: String,
        actualPrice: String,
        price: String,
        image: String,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            val product = ProductModel(
                id = UUID.randomUUID().toString(),
                title = name,
                description = description,
                category = category,
                actualPrice = actualPrice,
                price = price,
                images = listOf(image)
            )
            repository.addProduct(product)
            onDone()
        }
    }
}
