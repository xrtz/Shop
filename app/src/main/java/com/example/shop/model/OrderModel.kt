package com.example.shop.model

import com.google.firebase.Timestamp

data class OrderModel(
    val id: String = "",
    val userId: String = "",
    val date: Timestamp = Timestamp.now(),
    val items: Map<String, Long> = mapOf(),
    val address: String = "",
    val status: String = ""
)
