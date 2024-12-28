package com.example.torontorenthomecompose.models

data class Realtor(
    val uid: String = "",
    val firstName:String,
    val lastName:String,
    val email:String,
    val password:String,
    val realtorHouseIds:List<String>,
)