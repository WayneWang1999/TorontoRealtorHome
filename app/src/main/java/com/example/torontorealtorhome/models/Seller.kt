package com.example.torontorenthome.models

data class Seller(
    val firstName:String,
    val lastName:String,
    val email:String,
    val password:String,
    val sellerHouseIds:List<String>,
)
