package com.example.shop.model

import androidx.annotation.LongDef

data class UserModel (
    val name: String = "",
    val email: String = "",
    val Uid: String = "",
    val cartItems: Map<String, Long> = mapOf(),
    val address: String = "",
    val favItems: List<String> = listOf(),
    val admin: Boolean = false
    )