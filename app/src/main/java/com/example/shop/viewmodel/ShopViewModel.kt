package com.example.shop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.model.CategoryModel
import com.example.shop.model.ProductModel
import com.example.shop.model.UserModel
import com.example.shop.repository.Repository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShopViewModel(
    private val repository: Repository
) : ViewModel() {
    private val _user = MutableStateFlow<UserModel?>(null)
    val user: StateFlow<UserModel?> = _user


    private val _categories = MutableStateFlow<List<CategoryModel>>(emptyList())
    val categories: StateFlow<List<CategoryModel>> = _categories

    private val _cartItems = MutableStateFlow<Map<String, Int>>(emptyMap())
    val cartItems: StateFlow<Map<String, Int>> = _cartItems

    private var cartListener: ListenerRegistration? = null



    init {
        observeCart()
        observeUser()
    }

    private fun observeUser() {
        viewModelScope.launch {
            repository.userFlow().collect { newUser ->
                _user.value = newUser
            }
        }
    }


    private fun observeCart() {
        viewModelScope.launch {
            repository.cartItemsFlow().collect { newCart ->
                _cartItems.value = newCart
            }
        }
    }

    fun addToCart(productId: String) {
        viewModelScope.launch {
            repository.addToCart(productId)
        }
    }

    fun removeFromCart(productId: String, removeAll: Boolean = false) {
        viewModelScope.launch {
            repository.removeFromCart(productId, removeAll)
        }
    }


    override fun onCleared() {
        super.onCleared()
        cartListener?.remove()
    }




    fun loadCategories() {
        viewModelScope.launch {
            _categories.value = repository.getCategories()
        }
    }
    fun getUser(){
        viewModelScope.launch {
            _user.value = repository.getUser()
        }
    }

    fun updateAddress(address: String) {
        viewModelScope.launch {
            repository.updateAddress(address)
        }
    }



    private val _products = MutableStateFlow<List<ProductModel>>(emptyList())
    val products: StateFlow<List<ProductModel>> = _products

    fun loadProducts() {
        if (_products.value.isNotEmpty()) return
        viewModelScope.launch {
            _products.value = repository.getProducts()
        }
    }



    fun toggleFavorite(productId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(productId)
        }
    }
}
