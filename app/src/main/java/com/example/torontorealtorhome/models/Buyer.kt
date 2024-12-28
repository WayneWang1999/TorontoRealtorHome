package com.example.torontorenthome.models


data class Buyer(
    val uid: String = "",
    val firstName:String,
    val lastName:String,
    val email:String,
    val password:String,
    val favoriteHouseIds:List<String>,
)
