package com.example.shop.model

data class ProductModel(
    val id : String = "",
    val title : String = "",
    val description : String = "",
    val category : String = "",
    val actualPrice : String = "",
    val price : String = "",
    val images : List<String> = emptyList(),
    val otherDetails : Map<String, String> = mapOf()
)
