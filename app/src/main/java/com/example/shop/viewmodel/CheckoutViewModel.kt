package com.example.shop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.Util
import com.example.shop.model.ProductModel
import com.example.shop.model.UserModel
import com.example.shop.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CheckoutState(
    val user: UserModel? = null,
    val products: List<ProductModel> = emptyList(),
    val subTotal: Float = 0f,
    val fullPrice: Float = 0f,
    val discount: Float = 0f
)

class CheckoutViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _user = MutableStateFlow<UserModel?>(null)
    val user: StateFlow<UserModel?> = _user

    private val _products = MutableStateFlow<List<ProductModel>>(emptyList())
    val products: StateFlow<List<ProductModel>> = _products

    val state: StateFlow<CheckoutState> = combine(_user, _products) { user, products ->
        var subTotal = 0f
        var fullPrice = 0f

        user?.cartItems?.forEach { (id, qty) ->
            val product = products.find { it.id == id }
            if (product != null) {
                subTotal += qty * (product.actualPrice.toFloatOrNull() ?: 0f)
                fullPrice += qty * (product.price.toFloatOrNull() ?: 0f)
            }
        }

        CheckoutState(
            user = user,
            products = products,
            subTotal = subTotal,
            fullPrice = fullPrice,
            discount = fullPrice - subTotal
        )
    }.stateIn(viewModelScope, started = kotlinx.coroutines.flow.SharingStarted.Lazily, initialValue = CheckoutState())

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.userFlow().collect { user ->
                _user.value = user
                if (user.cartItems.isNotEmpty()) {
                    val productsInCart = repository.getProducts().filter { it.id in user.cartItems.keys }
                    _products.value = productsInCart
                } else {
                    _products.value = emptyList()
                }
            }
        }
    }

    fun completeOrder() {
        viewModelScope.launch {
            Util.clearBusketAndAddToOrder()
        }
    }
}
