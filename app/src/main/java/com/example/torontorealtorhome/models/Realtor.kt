package com.example.torontorenthomecompose.models

data class Realtor(
    val firstName:String,
    val lastName:String,
    val email:String,
    val password:String,
    val realtorHouseIds:List<String>,
)