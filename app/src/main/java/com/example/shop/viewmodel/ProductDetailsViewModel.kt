package com.example.shop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.model.ProductModel
import com.example.shop.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _product = MutableStateFlow<ProductModel?>(null)
    val product: StateFlow<ProductModel?> = _product

    fun loadProduct(productId: String) {
        viewModelScope.launch {
            val allProducts = repository.getProducts()
            val found = allProducts.find { it.id == productId }
            _product.value = found ?: ProductModel()
        }
    }

    fun addToCart(productId: String) {
        viewModelScope.launch {
            repository.addToCart(productId)
        }
    }

    fun toggleFavorite(productId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(productId)
        }
    }
}
